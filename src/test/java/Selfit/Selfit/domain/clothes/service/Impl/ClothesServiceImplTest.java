package Selfit.Selfit.domain.clothes.service.Impl;

import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//이 어노테이션 속성으로 스프링 애플리케이션 컨텍스트를 로드할 때
//application.properties 또는 @Value("${file.upload-dir}") 로 바인딩되는
//file.upload-dir 프로퍼티를 JVM 시스템 임시 디렉터리(java.io.tmpdir)로 오버라이드합니다.
//예컨대 Windows에서는 보통 C:\Users\<사용자>\AppData\Local\Temp 가 되고,
//Linux/macOS에서는 /tmp 등이 됩니다.
@SpringBootTest(properties = "file.upload-dir=${java.io.tmpdir}")
@Transactional
public class ClothesServiceImplTest {
    @Autowired private WardrobeRepository wardrobeRepository;
    @Autowired private ClothesService clothesService;
    @Autowired private UserRepository userRepository;
    @Autowired private ClothesRepository clothesRepository;

    // junit5가 매 테스트마다 고유한 임시 디렉터리 tempDir을 생성해 주입.
    @TempDir Path tempDir;

    @BeforeEach
    public void setUp() throws Exception {
        //사실 resolve("") 는 tempDir 자기 자신을 가리키므로, 이 줄은 tempDir이 존재하지 않을 때 생성해 줍니다.
        Files.createDirectories(tempDir.resolve(""));
//        System.setProperty("file.upload-dir", tempDir.resolve("uploads-test").toString());
        // 이미 springboottest에 properties 설정했으므로 필요 x
    }

    /**
     *
     * 담은 옷 등록, 삭제, 제공
     */
    @Test
    void uploadAndDeleteAndExportByPath() throws IOException {
        // Upload
        byte[] data = "test-data".getBytes();
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", data);

        User user = User.builder()
                .accountId("test1")
                .password("testpwd")
                .email("test@example.com")
                .build();
        Wardrobe w = Wardrobe.builder()
                .user(user)
                .build();
        user.setWardrobe(w);
        userRepository.save(user);
        wardrobeRepository.save(w);
        Long userId = user.getId();

        String path = clothesService.saveClothes(userId, ClothesType.BOTTOM, file);
        assertNotNull(path);
        // Verify saved entity
        Optional<Clothes> opt = clothesRepository.findByPath(path);
        assertTrue(opt.isPresent());
        Clothes saved = opt.get();
        assertEquals(ClothesType.BOTTOM, saved.getType());

        // Export
        Resource res = clothesService.provideClothes(path);
        assertTrue(res.exists());
        byte[] content = Files.readAllBytes(Path.of(path));
        byte[] returned;
        try(InputStream is = res.getInputStream()){
            returned = is.readAllBytes();
        }
        assertArrayEquals(content, returned);

        // Delete
        String deleted = clothesService.deleteClothes(path);
        assertEquals(path, deleted);
        assertFalse(clothesRepository.findByPath(path).isPresent());
        assertFalse(Files.exists(Path.of(path)));
    }
}