package Selfit.Selfit.domain.clothes.service.Impl;

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

import selfit.selfit.domain.clothes.dto.ClothesType;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.clothes.repository.ClothesRepository;
import selfit.selfit.domain.clothes.service.ClothesService;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;
import selfit.selfit.domain.wardrobe.repository.WardrobeRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//이 어노테이션 속성으로 스프링 애플리케이션 컨텍스트를 로드할 때
//application.properties 또는 @Value("${file.upload-dir}") 로 바인딩되는
//file.upload-dir 프로퍼티를 JVM 시스템 임시 디렉터리(java.io.tmpdir)로 오버라이드합니다.
//예컨대 Windows에서는 보통 C:\Users\<사용자>\AppData\Local\Temp 가 되고,
//Linux/macOS에서는 /tmp 등이 됩니다.
@SpringBootTest(properties = "file.upload-dir=${java.io.tmpdir}/cart-test")
@Transactional
public class ClothesServiceImplTest {

    @Autowired private ClothesService clothesService;
    @Autowired private UserRepository userRepository;
    @Autowired private ClothesRepository clothesRepository;

    // junit5가 매 테스트마다 고유한 임시 디렉터리 tempDir을 생성해 주입.
    @TempDir Path tempDir;

    private User user;

    @BeforeEach
    void setUp() throws IOException {
        Path uploadDir = tempDir.resolve("cart-test");
        Files.createDirectories(uploadDir);

        user = User.builder()
                .accountId("testuser")
                .password("password")
                .email("test@example.com")
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("담은 옷 등록")
    void testSaveClothes() throws IOException {
        byte[] data = "image-content".getBytes();
        MultipartFile file = new MockMultipartFile(
                "file", "item.png", "image/png", data);

        String path = clothesService.saveClothes(user.getId(), ClothesType.BOTTOM, file);

        Path p = Path.of(path);
        assertThat(Files.exists(p)).isTrue();

        Optional<Clothes> opt = clothesRepository.findByPath(path);
        assertThat(opt).isPresent();

        Clothes saved = opt.get();
        assertThat(saved.getUser().getId()).isEqualTo(user.getId());
        assertThat(saved.getType()).isEqualTo(ClothesType.BOTTOM);
        assertThat(saved.getPath()).isEqualTo(path);
    }

    @Test
    @DisplayName("담은 옷 삭제")
    void testDeleteClothes() throws IOException {
        MultipartFile f1 = new MockMultipartFile("file", "a.png", "image/png", "A".getBytes());
        MultipartFile f2 = new MockMultipartFile("file", "b.png", "image/png", "B".getBytes());
        String path1 = clothesService.saveClothes(user.getId(), ClothesType.BOTTOM, f1);
        String path2 = clothesService.saveClothes(user.getId(), ClothesType.TOP, f2);

        List<String> remaining = clothesService.deleteClothes(user.getId(), 0);
        assertThat(remaining).containsExactly(path2);

        assertThat(Files.exists(Path.of(path1))).isFalse();

        assertThat(clothesRepository.findByPath(path1)).isEmpty();
        assertThat(clothesRepository.findByPath(path2)).isPresent();
    }

    @Test
    @DisplayName("담은 옷 제공")
    void testProvideClothes() throws Exception {
        byte[] data = "test-data".getBytes();
        MultipartFile file = new MockMultipartFile("file", "sample.png", "image/png", data);
        clothesService.saveClothes(user.getId(), ClothesType.BOTTOM, file);

        Resource resource = clothesService.provideClothes(user.getId(), 0);
        assertThat(resource.exists()).isTrue();
        try (InputStream is = resource.getInputStream()) {
            byte[] actual = is.readAllBytes();
            assertThat(actual).isEqualTo(data);
        }
    }

    @Test
    @DisplayName("담은 옷 제공 인덱스 유효성 검사")
    void testProvideClothes_invalidIndex() {
        assertThrows(IllegalArgumentException.class,
                () -> clothesService.provideClothes(user.getId(), 5));
    }
}