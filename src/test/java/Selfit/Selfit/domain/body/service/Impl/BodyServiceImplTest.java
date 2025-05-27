package Selfit.Selfit.domain.body.service.Impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.body.repository.BodyRepository;
import selfit.selfit.domain.body.service.BodyService;
import selfit.selfit.domain.image.ImageFileStorageService;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.user.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class BodyServiceImplTest {

    @Autowired private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BodyRepository bodyRepository;

    @Autowired
    private ImageFileStorageService imageFileStorageService;

    @Autowired private BodyService bodyService;

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
}