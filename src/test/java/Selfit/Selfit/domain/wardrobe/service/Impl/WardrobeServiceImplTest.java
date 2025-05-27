package Selfit.Selfit.domain.wardrobe.service.Impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
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
import selfit.selfit.domain.wardrobe.dto.WardrobeDto;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;
import selfit.selfit.domain.wardrobe.repository.WardrobeRepository;
import selfit.selfit.domain.wardrobe.service.WardrobeService;

import java.io.IOException;
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
public class WardrobeServiceImplTest {

    @Autowired private BodyRepository bodyRepository;
    @Autowired private ClothesService clothesService;
    @Autowired private UserRepository userRepository;
    @Autowired private ClothesRepository clothesRepository;
    @Autowired private WardrobeRepository wardrobeRepository;
    @Autowired private WardrobeService wardrobeService;
    @Autowired private Environment env;

    private Path uploadDir;
    private Long userId;

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

    @Test
    @DisplayName("소장 의류 등록")
    public void saveClothes() throws IOException {
        byte[] content = "C:\\Users\\deukr\\Capstone\\Image\\imageFile.png".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "imageFile.png", MediaType.IMAGE_PNG_VALUE, content);

        // when
        ClothesDto dto = clothesService.saveClothes(userId, ClothesType.TOP, file);

        Wardrobe wardrobe = wardrobeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(""));

        Clothes findClothes = Clothes.builder()
                .file_path(dto.getPath())
                .type(dto.getType())
                .build();

        findClothes.setWardrobe(wardrobe);

        clothesRepository.save(findClothes);

        WardrobeDto wardrobeDto = wardrobeService.saveClothes(userId, findClothes.getId());

        if(wardrobeDto.getC().getFile_path().equals(findClothes.getFile_path()) && wardrobeDto.getC().getType()==findClothes.getType()){
            System.out.println("Success");
        }else{
            System.out.println("Failed");
        }

    }

    @Test
    @DisplayName("소장 의류 삭제")
    public void deleteClothes() throws IOException {
        byte[] content = "C:\\Users\\deukr\\Capstone\\Image\\imageFile.png".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "imageFile.png", MediaType.IMAGE_PNG_VALUE, content);

        // when
        ClothesDto dto = clothesService.saveClothes(userId, ClothesType.TOP, file);

        Wardrobe wardrobe = wardrobeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(""));

        Clothes findClothes = Clothes.builder()
                .file_path(dto.getPath())
                .type(dto.getType())
                .build();

        findClothes.setWardrobe(wardrobe);

        clothesRepository.save(findClothes);

        WardrobeDto wardrobeDto = wardrobeService.saveClothes(userId, findClothes.getId());

        Clothes c = wardrobe.getClothesList().get(0);

        wardrobeService.removeClothes(userId, findClothes.getId());

        // then
        if(wardrobe.getClothesList().isEmpty()){
            System.out.println("Success");
        }
        else{
            System.out.println("Failed");
        }

        Path stored = uploadDir.resolve(c.getFile_path());
        assertThat(Files.exists(stored)).isTrue();


    }

    @Test
    @DisplayName("소장 의류 제공")
    public void provideClothes_shouldReturnCorrectDto() throws Exception {
        // 먼저 save
        // 이미지가 있는 로컬 파일 경로
        byte[] content = "C:\\Users\\deukr\\Capstone\\Image\\imageFile.png".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file", "imageFile.png", MediaType.IMAGE_PNG_VALUE, content);
        ClothesDto dto1 = clothesService.saveClothes(userId, ClothesType.TOP, file);
        ClothesDto dto2 = clothesService.saveClothes(userId, ClothesType.TOP, file);

        Wardrobe wardrobe = wardrobeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(""));

        Clothes findClothes1 = Clothes.builder()
                .file_path(dto1.getPath())
                .type(dto1.getType())
                .build();

        Clothes findClothes2 = Clothes.builder()
                .file_path(dto2.getPath())
                .type(dto2.getType())
                .build();

        findClothes1.setWardrobe(wardrobe);
        findClothes2.setWardrobe(wardrobe);

        Clothes c1 = clothesRepository.save(findClothes1);
        Clothes c2 = clothesRepository.save(findClothes2);

        wardrobeService.saveClothes(userId, c1.getId());
        wardrobeService.saveClothes(userId, c2.getId());

        WardrobeDto wdto2 = wardrobeService.provideClothes(userId, findClothes1.getId());

        assertThat(c1).isEqualTo(wdto2.getC());
//        assertThat(c2).isEqualTo(wdto2.getC()); // 오류 발생해야함.

    }
}