package Selfit.Selfit.domain.wardrobe.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.clothes.repository.ClothesRepository;
import selfit.selfit.domain.image.ImageFileStorageService;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;
import selfit.selfit.domain.wardrobe.repository.WardrobeRepository;
import selfit.selfit.domain.wardrobe.service.WardrobeService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "file.upload-dir=${java.io.tmpdir}")
@Transactional
public class WardrobeServiceImplTest {

    @Autowired private WardrobeService wardrobeService;
    @Autowired private UserRepository userRepository;
    @Autowired private WardrobeRepository wardrobeRepository;
    @Autowired private ClothesRepository clothesRepository;
    @Autowired private ImageFileStorageService imageFileStorageService;

    private User user;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        Path uploadDir = tempDir.resolve("");
        Files.createDirectories(uploadDir);
        System.setProperty("file.upload-dir", uploadDir.toString());

        user = User.builder().accountId("testuser").password("pass").email("test@example.com").build();
        Wardrobe w = Wardrobe.builder()
                .user(user)
                .build();
        Body b = Body.builder().user(user).build();
        user.setWardrobe(w);
        user.setBody(b);
        userRepository.save(user);
        wardrobeRepository.save(w);
    }

    @Test
    @DisplayName("소장 의류 등록")
    void testSaveClothesWardrobe() throws IOException {
        byte[] img1 = "Image".getBytes();
        byte[] img2 = "Image".getBytes();
        MultipartFile file1 = new MockMultipartFile("files", "a.png", "image/png", img1);
        MultipartFile file2 = new MockMultipartFile("files", "b.jpg", "image/png", img2);
        List<MultipartFile> files = List.of(file1, file2);

        List<String> paths = wardrobeService.saveClothes(user.getId(), files);

        assertThat(paths).hasSize(2);

        for (String pathStr : paths) {
            Path p = Path.of(pathStr);
            assertThat(Files.exists(p)).isTrue();
        }

        Wardrobe updated = wardrobeRepository.findByUserId(user.getId()).orElseThrow();
        assertThat(updated.getClothesPhotos()).containsExactlyInAnyOrderElementsOf(paths);
    }

    @Test
    @DisplayName("소장 의류 삭제")
    void testDeleteClothesWardrobe() throws IOException {
        byte[] img1 = "img1".getBytes();
        byte[] img2 = "img2".getBytes();

        MultipartFile file1 = new MockMultipartFile("files", "x1.png", "image/png", img1);
        MultipartFile file2 = new MockMultipartFile("files", "x2.png", "image/png", img2);
        List<String> paths = wardrobeService.saveClothes(user.getId(), List.of(file1, file2));
        String toDelete = paths.get(0);
        assertThat(paths).contains(toDelete);
        assertThat(Files.exists(Path.of(toDelete))).isTrue();

        List<String> remaining = wardrobeService.deleteClothes(user.getId(), toDelete);
        assertThat(remaining).doesNotContain(toDelete);
        assertThat(Files.exists(Path.of(toDelete))).isFalse();

        Wardrobe updated = wardrobeRepository.findByUserId(user.getId()).orElseThrow();
        assertThat(updated.getClothesPhotos()).doesNotContain(toDelete);
    }


    @Test
    @DisplayName("소장 의류 리소스 제공")
    void provideClothesResource() throws Exception {
        // Upload a test image
        byte[] data = "test-image-content".getBytes();
        MultipartFile file = new MockMultipartFile(
                "files", "sample.png", "image/png", data);
        List<String> paths = wardrobeService.saveClothes(user.getId(), List.of(file));
        assertFalse(paths.isEmpty());
        String path = paths.get(0);

        // Retrieve as Resource
        Resource resource = wardrobeService.provideClothesResource(user.getId(), 0);
        assertNotNull(resource);
        assertTrue(resource.exists());

        // Compare file bytes
        byte[] expected = Files.readAllBytes(Path.of(path));
        try (InputStream is = resource.getInputStream()) {
            byte[] actual = is.readAllBytes();
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("소장 의류 인덱스 적합 테스트")
    void provideClothesResource_invalidIndex() {
        // No files uploaded, index out of bounds
        assertThrows(IllegalArgumentException.class, () ->
                wardrobeService.provideClothesResource(user.getId(), 1)
        );
    }
}