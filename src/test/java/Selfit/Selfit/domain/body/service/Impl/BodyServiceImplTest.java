package Selfit.Selfit.domain.body.service.Impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.body.repository.BodyRepository;
import selfit.selfit.domain.body.service.BodyService;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.dto.UserDetailDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class BodyServiceImplTest {

    @Autowired private BodyService bodyService;
    @Autowired private UserService userService;

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
        dto.setWaist("42ch");

        Body savedBody = bodyService.saveSize(user.getId(), dto);

        assertThat(savedBody.getId()).isEqualTo(user.getId());
        assertThat(savedBody.getHeight()).isEqualTo(dto.getHeight());
        assertThat(savedBody.getWeight()).isEqualTo(dto.getWeight());
        assertThat(savedBody.getWaist()).isEqualTo(dto.getWaist());

    }
    @Test
    @DisplayName("신체 정보 업데이트 테스트")
    void updateSizeTest() {
        UserAccountDto uDto = new UserAccountDto("user2","pw2","test2@example.com");
        User user = userService.registerUser(uDto);

        BodySizeDto first = new BodySizeDto("165cm","65kg","40ch");
        Body b1 = bodyService.saveSize(user.getId(), first);

        BodySizeDto update = new BodySizeDto("168cm","68kg","41ch");
        Body b2 = bodyService.saveSize(user.getId(), update);


        assertThat(b2.getId()).isEqualTo(b1.getId());
        assertThat(b2.getHeight()).isEqualTo("168cm");
        assertThat(b2.getWeight()).isEqualTo("68kg");
        assertThat(b2.getWaist()).isEqualTo("41ch");
    }
}