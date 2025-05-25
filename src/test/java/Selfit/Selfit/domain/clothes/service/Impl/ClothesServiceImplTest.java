package Selfit.Selfit.domain.clothes.service.Impl;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.body.repository.BodyRepository;
import selfit.selfit.domain.clothes.dto.ClothesDto;
import selfit.selfit.domain.clothes.dto.ClothesType;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.clothes.repository.ClothesRepository;
import selfit.selfit.domain.clothes.service.ClothesService;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;
import selfit.selfit.domain.wardrobe.repository.WardrobeRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "file.upload-dir=C:\\\\Users\\\\deukr\\\\Capstone\\\\Image")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClothesServiceImplTest {

    @Autowired private BodyRepository bodyRepository;
    @Autowired private ClothesService clothesService;
    @Autowired private UserRepository userRepository;
    @Autowired private ClothesRepository clothesRepository;
    @Autowired private WardrobeRepository wardrobeRepository;
    @Autowired private Environment env;
    private Long userId;
    private Path uploadDir; // 로컬파일 경로

    @BeforeEach
    public void setUp() throws Exception {
        String dir = env.getProperty("file.upload-dir");
        uploadDir = Paths.get(dir).toAbsolutePath().normalize();

        if (Files.exists(uploadDir)) {
            Files.walk(uploadDir)
                    .filter(Files::isRegularFile)
                    .forEach(p -> p.toFile().delete());
        } else {
            Files.createDirectories(uploadDir);
        }

        User user = User.builder()
                .name("test")
                .age(30)
                .email("test@example.com")
                .accountId("testAccount")
                .password("testPwd")
                .nickname("tester")
                .gender("M")
                .build();

        Body body = Body.builder()
                .user(user)
                .build();

        Wardrobe wardrobe = Wardrobe.builder()
                .user(user)
                .build();

        user.setBody(body);
        user.setWardrobe(wardrobe);

        userRepository.save(user);
        userId = user.getId();

    }

    /**
     *
     * 담은 옷 등록
     */
    @Test
    public void saveClothes_shouldPersistEntityAndStoreFile() throws Exception {
        // given
        // 이미지가 있는 로컬 파일 경로
        byte[] content = "C:\\Users\\deukr\\Capstone\\Image\\imageFile.png".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "imageFile.png", MediaType.IMAGE_PNG_VALUE, content);

        // when
        ClothesDto dto = clothesService.saveClothes(userId, ClothesType.TOP, file);

        // then – DB 검증
        List<Clothes> all = clothesRepository.findAll();
        assertThat(all).hasSize(1);
        Clothes saved = all.get(0);
        assertThat(saved.getType()).isEqualTo(ClothesType.TOP);
        assertThat(dto.getPath()).isEqualTo(saved.getFile_path());

        // then – 파일 시스템 검증
        Path stored = uploadDir.resolve(saved.getFile_path());
        assertThat(Files.exists(stored)).isTrue();
        assertThat(Files.readAllBytes(stored)).contains(content);
    }

    /**
     *
     * 담은 옷 제공
     */
    @Test
    public void provideClothes_shouldReturnCorrectDto() throws Exception {
        // 먼저 save
        // 이미지가 있는 로컬 파일 경로
        byte[] content = "C:\\Users\\deukr\\Capstone\\Image\\imageFile.png".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "imageFile.png", MediaType.IMAGE_PNG_VALUE, content);

        ClothesDto savedDto = clothesService.saveClothes(userId, ClothesType.BOTTOM, file);

        Clothes saved = clothesRepository.findAll().get(0);

        // when
        ClothesDto dto = clothesService.provideClothes(saved.getId());

        // then
        assertThat(dto.getPath()).isEqualTo(savedDto.getPath());
        assertThat(dto.getType()).isEqualTo(savedDto.getType());
    }


    /**
     *
     * 담은 옷 삭제
     */

    @Test
    public void deleteClothes_shouldRemoveEntityAndFile() throws Exception {
        // 먼저 save
        // 이미지가 있는 로컬 파일 경로
        byte[] content = "C:\\Users\\deukr\\Capstone\\Image\\imageFile.png".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "imageFile.png", MediaType.IMAGE_PNG_VALUE, content);

        ClothesDto savedDto = clothesService.saveClothes(userId, ClothesType.TOP, file);
        Clothes saved = clothesRepository.findAll().get(0);

        Path stored = uploadDir.resolve(savedDto.getPath());
        assertThat(Files.exists(stored)).isTrue();

        // when
        clothesService.deleteClothes(saved.getId());

        // then – DB 에서 삭제
        assertThat(clothesRepository.findById(saved.getId())).isEmpty();
        // 파일 시스템에서도 삭제
        assertThat(Files.exists(stored)).isFalse();
    }
}