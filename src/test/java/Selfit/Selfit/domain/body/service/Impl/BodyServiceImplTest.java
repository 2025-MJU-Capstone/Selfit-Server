package Selfit.Selfit.domain.body.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.body.repository.BodyRepository;
import selfit.selfit.domain.body.service.BodyService;
import selfit.selfit.domain.image.ImageFileStorageService;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.user.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(properties = "file.upload-dir=${java.io.tmpdir}")
@Transactional
class BodyServiceImplTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private BodyRepository bodyRepository;
    @Autowired private ImageFileStorageService imageFileStorageService;
    @Autowired private BodyService bodyService;

    @TempDir Path tmpDir;


    @BeforeEach
    void setUp() throws IOException {
        // Configure upload directory to a clean temp folder
        Path uploadDir = tmpDir.resolve("");
        Files.createDirectories(uploadDir);
    }
    @Test
    @DisplayName("초기 신체 정보 저장 테스트")
    void saveSizeTest() {

        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setAccountId("testAccount");
        userAccountDto.setPassword("testPassword");
        userAccountDto.setEmail("test@example.com");

        User user = userService.registerUser(userAccountDto);

        BodySizeDto dto = new BodySizeDto();
        dto.setHeight("170cm");
        dto.setWeight("76kg");
        dto.setWaist("42inch");
        dto.setLeg("62cm");
        dto.setShoulder("56inch");
        dto.setPelvis("32inch");
        dto.setChest("64cm");

        Body savedBody = bodyService.saveSize(user.getId(), dto);

        assertThat(savedBody.getId()).isNotNull();
        assertThat(savedBody.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedBody.getHeight()).isEqualTo(dto.getHeight());
        assertThat(savedBody.getWeight()).isEqualTo(dto.getWeight());
        assertThat(savedBody.getWaist()).isEqualTo(dto.getWaist());
        assertThat(savedBody.getLeg()).isEqualTo(dto.getLeg());
        assertThat(savedBody.getShoulder()).isEqualTo(dto.getShoulder());
        assertThat(savedBody.getPelvis()).isEqualTo(dto.getPelvis());
        assertThat(savedBody.getChest()).isEqualTo(dto.getChest());

    }
    @Test
    @DisplayName("신체 정보 업데이트 테스트")
    void updateSizeTest() {
        UserAccountDto uDto = new UserAccountDto("user2","pw2","test2@example.com");
        User user = userService.registerUser(uDto);

        BodySizeDto dto = new BodySizeDto();
        dto.setHeight("170cm");
        dto.setWeight("76kg");
        dto.setWaist("42inch");
        dto.setLeg("62cm");
        dto.setShoulder("56inch");
        dto.setPelvis("32inch");
        dto.setChest("64cm");
        bodyService.saveSize(user.getId(), dto);

        Body b1 = bodyService.saveSize(user.getId(), dto);

        BodySizeDto update = new BodySizeDto();
        update.setHeight("168cm");
        update.setWeight("68kg");
        update.setWaist("41inch");
        update.setLeg("22cm");
        update.setShoulder("325inch");
        update.setPelvis("32inch");
        update.setChest("78cm");

        Body b2 = bodyService.saveSize(user.getId(), update);

        assertThat(b2.getId()).isEqualTo(b1.getId());
        assertThat(b2.getHeight()).isEqualTo("168cm");
        assertThat(b2.getWeight()).isEqualTo("68kg");
        assertThat(b2.getWaist()).isEqualTo("41inch");
    }

    @Test
    @DisplayName("전신 사진 업로드 테스트")
    void uploadFullBodyTest() throws IOException {
        UserAccountDto dto = new UserAccountDto("user2", "pw2", "test2@example.com");
        User user = userService.registerUser(dto);

        // Prepare exactly 4 mock image files
        List<MultipartFile> files = IntStream.range(0, 4)
                .mapToObj(i -> (MultipartFile)new MockMultipartFile("files",
                        i + ".png",
                        "image/png",
                        ("data" + i).getBytes()
                ))
                .toList();

        // Execute service method
        List<String> paths = bodyService.uploadFullBody(user, files);

        // 1) Return value size
        assertThat(paths).hasSize(4);

        // 2) Each path points to an existing file
        for (String p : paths) {
            Path path = Path.of(p);
            assertThat(Files.exists(path)).isTrue();
        }

        // 3) Body entity has been created/updated with these paths
        Optional<Body> opt = bodyRepository.findByUser(user);
        assertThat(opt).isPresent();
        Body b = opt.get();
        assertThat(b.getFullBodyPhotos())
                .containsExactlyInAnyOrderElementsOf(paths);

    }

    @Test
    @DisplayName("얼굴 사진 업로드 테스트")
    void uploadFaceTest() throws IOException {
        UserAccountDto dto = new UserAccountDto("user2", "pw2", "test2@example.com");
        User user = userService.registerUser(dto);

        // Prepare exactly 4 mock image files
        List<MultipartFile> files = IntStream.range(0, 4)
                .mapToObj(i -> (MultipartFile)new MockMultipartFile("files",
                        i + ".png",
                        "image/png",
                        ("data" + i).getBytes()
                ))
                .toList();

        // Execute service method
        List<String> paths = bodyService.uploadFullBody(user, files);

        // 1) Return value size
        assertThat(paths).hasSize(4);

        // 2) Each path points to an existing file
        for (String p : paths) {
            Path path = Path.of(p);
            assertThat(Files.exists(path)).isTrue();
        }

        // 3) Body entity has been created/updated with these paths
        Optional<Body> opt = bodyRepository.findByUser(user);
        assertThat(opt).isPresent();
        Body b = opt.get();
        assertThat(b.getFullBodyPhotos())
                .containsExactlyInAnyOrderElementsOf(paths);

    }

    @Test
    @DisplayName("전신 사진이 4장이 아닐 때")
    void uploadFullBody_invalidCount() {
        // Too few files (3)
        UserAccountDto dto = new UserAccountDto("user2", "pw2", "test2@example.com");
        User user = userService.registerUser(dto);

        List<MultipartFile> files3 = List.of(
                new MockMultipartFile("files", "a.png", "image/png", new byte[1]),
                new MockMultipartFile("files", "b.png", "image/png", new byte[1]),
                new MockMultipartFile("files", "c.png", "image/png", new byte[1])
        );
        assertThrows(IllegalArgumentException.class, () ->
                bodyService.uploadFullBody(user, files3)
        );

        // Null list
        assertThrows(IllegalArgumentException.class, () ->
                bodyService.uploadFullBody(user, null)
        );
    }


    @Test
    @DisplayName("얼굴 사진 업로드 성공")
    void uploadFacePhotos_success() throws IOException {
        // prepare 3 face images
        UserAccountDto dto = new UserAccountDto("user2", "pw2", "test2@example.com");
        User user = userService.registerUser(dto);

        List<MultipartFile> files = IntStream.range(0, 3)
                .mapToObj(i -> (MultipartFile)new MockMultipartFile(
                        "files",
                        i + ".jpg",
                        "image/jpeg",
                        ("face" + i).getBytes()
                )).toList();

        // call service
        List<String> paths = bodyService.uploadFace(user, files);

        // verify returned paths and file existence
        assertThat(paths).hasSize(3);
        for (String p : paths) {
            Path path = Path.of(p);
            assertThat(Files.exists(path)).isTrue();
        }

        // verify Body entity updated
        Body body = bodyRepository.findByUser(user).orElseThrow();
        assertThat(body.getFacePhotos()).containsExactlyInAnyOrderElementsOf(paths);
    }

    @Test
    @DisplayName("사진이 없을 때")
    void uploadFacePhotos_emptyList() {
        UserAccountDto dto = new UserAccountDto("user2", "pw2", "test2@example.com");
        User user = userService.registerUser(dto);

        assertThrows(IllegalArgumentException.class, () ->
                bodyService.uploadFace(user, List.of())
        );
    }

    @Test
    @DisplayName("사진이 9장 초과일 때")
    void uploadFacePhotos_tooManyFiles() {
        // prepare 10 files
        UserAccountDto dto = new UserAccountDto("user2", "pw2", "test2@example.com");
        User user = userService.registerUser(dto);

        List<MultipartFile> files = IntStream.range(0, 10)
                .mapToObj(i -> (MultipartFile)new MockMultipartFile(
                        "files",
                        i + ".png",
                        "image/png",
                        new byte[1]
                )).toList();

        assertThrows(IllegalArgumentException.class, () ->
                bodyService.uploadFace(user, files)
        );
    }
}