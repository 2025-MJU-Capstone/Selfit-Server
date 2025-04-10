package Selfit.Selfit.domain.user.service.Impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.dto.UserDetailDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.user.service.UserService;

@SpringBootTest
@Transactional
public class UserServiceImplTest {

    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 로직 통합 테스트")
    public void testRegisterUser(){
        //given
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setAccountId("testAccount");
        userAccountDto.setPassword("testPassword");
        userAccountDto.setEmail("test@example.com");

        //when
        User savedUser = userService.registerUser(userAccountDto);

        //then
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals("testAccount", savedUser.getAccountId());
        Assertions.assertTrue(passwordEncoder.matches("testPassword", savedUser.getPassword()));
        Assertions.assertEquals("test@example.com", savedUser.getEmail());

//        // db에 저장 되었는지 확인
//        User foundUser = userRepository.findById(savedUser.getId())
//                .orElseThrow(() -> new AssertionError("User not found in database"));
//
//        Assertions.assertTrue(passwordEncoder.matches("testPassword", foundUser.getPassword())); // db 데이터 비밀번호 검증
    }

    @Test
    @DisplayName("회원가입 중복 계정 검사 테스트")
    public void testDuplicateAccount(){
        //given
        UserAccountDto userAccountDto1 = new UserAccountDto();
        userAccountDto1.setAccountId("testAccount");
        userAccountDto1.setPassword("testPassword");
        userAccountDto1.setEmail("test1@example.com");

        UserAccountDto userAccountDto2 = new UserAccountDto();
        userAccountDto2.setAccountId("testAccount"); // 중복 계정 ID
        userAccountDto2.setPassword("anotherPassword");
        userAccountDto2.setEmail("test2@example.com");

        //when
        userService.registerUser(userAccountDto1);

        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            userService.registerUser(userAccountDto2);
        });
    }

    @Test
    @DisplayName("개인정보 등록 로직 통합 테스트")
    public void testUpdateUserDetails(){
        //given
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setAccountId("testAccount");
        userAccountDto.setPassword("testPassword");
        userAccountDto.setEmail("test@example.com");

        User savedUser = userService.registerUser(userAccountDto);

        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setName("John Doe");
        userDetailDto.setAge(25);
        userDetailDto.setNickname("johnny");
        userDetailDto.setGender("Male");

        //when
        User updatedUser = userService.updateUserDetails(savedUser.getId(), userDetailDto);

        //then
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("John Doe", updatedUser.getName());
        Assertions.assertEquals(25, updatedUser.getAge());
        Assertions.assertEquals("johnny", updatedUser.getNickname());
        Assertions.assertEquals("Male", updatedUser.getGender());
    }
}