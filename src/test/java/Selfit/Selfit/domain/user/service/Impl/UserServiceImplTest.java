package Selfit.Selfit.domain.user.service.Impl;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.dto.UserDetailDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.user.service.UserService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
        assertNotNull(savedUser.getId());
        assertEquals("testAccount", savedUser.getAccountId());
        assertTrue(passwordEncoder.matches("testPassword", savedUser.getPassword()));
        assertEquals("test@example.com", savedUser.getEmail());

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
        assertThrows(IllegalArgumentException.class, () ->{
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
        User updatedUser = userService.updateUserDetails(userDetailDto, savedUser.getAccountId());

        //then
        assertNotNull(updatedUser);
        assertEquals("John Doe", updatedUser.getName());
        assertEquals(25, updatedUser.getAge());
        assertEquals("johnny", updatedUser.getNickname());
        assertEquals("Male", updatedUser.getGender());
    }

    @Test
    @DisplayName("개인정보 변경 통합 테스트")
    public void testUpdateUserDetails1(){
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
        User updatedUser = userService.updateUserDetails(userDetailDto, savedUser.getAccountId());

        UserDetailDto userDetailDto1 = new UserDetailDto();
        userDetailDto1.setName("hot");
        userDetailDto1.setAge(48);
        userDetailDto1.setNickname("less");
        userDetailDto1.setGender("Female");

        updatedUser = userService.updateUserDetails(userDetailDto1, savedUser.getAccountId());

        //then
        assertNotNull(updatedUser);
        assertEquals("hot", updatedUser.getName());
        assertEquals(48, updatedUser.getAge());
        assertEquals("less", updatedUser.getNickname());
        assertEquals("Female", updatedUser.getGender());
    }

    @Test
    @DisplayName("아이디 찾기")
    public void findIdTest(){
        // 회원가입
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setAccountId("testAccount");
        userAccountDto.setPassword("testPassword");
        userAccountDto.setEmail("test@example.com");

        User user = userService.registerUser(userAccountDto);

        String email = userAccountDto.getEmail();
//        String email = "test2@example.com"; // 실패하는 로직
        String findAccount = userService.findAccountId(email);

        assertThat(user.getAccountId()).isEqualTo(findAccount);
    }

    @Test
    @DisplayName("비밀번호 재설정 및 복구")
    public void findPwdTest(){
        // 회원가입
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setAccountId("testAccount");
        userAccountDto.setPassword("testPassword");
        userAccountDto.setEmail("test@example.com");

        User user = userService.registerUser(userAccountDto);

        String accountId = userAccountDto.getAccountId();
        String email = userAccountDto.getEmail();

        String tempPwd = userService.recoverPassword(accountId, email);

        assertTrue(passwordEncoder.matches(tempPwd, user.getPassword()));

        System.out.println("userPwd = " + user.getPassword());

        String newPwd = "realPassword";

        userService.resetPassword(accountId, user.getPassword(), newPwd, newPwd);

        assertTrue(passwordEncoder.matches(newPwd, user.getPassword()));

        System.out.println("userPwd = " + user.getPassword());
    }

    @Test
    @DisplayName("닉네임 중복 확인")
    public void duplicateNicknameTest(){
        // 회원가입
        UserAccountDto userAccountDto1 = new UserAccountDto();
        userAccountDto1.setAccountId("testAccount1");
        userAccountDto1.setPassword("testPassword1");
        userAccountDto1.setEmail("test1@example.com");

        User user1 = userService.registerUser(userAccountDto1);

        // 개인정보 등록
        UserDetailDto userDetailDto1 = new UserDetailDto();
        userDetailDto1.setName("John Doe");
        userDetailDto1.setAge(25);
        userDetailDto1.setNickname("johnny");
        userDetailDto1.setGender("Male");

        User updateUser1 = userService.updateUserDetails(userDetailDto1, user1.getAccountId());


        // 회원가입
        UserAccountDto userAccountDto2 = new UserAccountDto();
        userAccountDto2.setAccountId("testAccount2");
        userAccountDto2.setPassword("testPassword2");
        userAccountDto2.setEmail("test2@example.com");

        User user2 = userService.registerUser(userAccountDto2);

        // 개인정보 등록
        UserDetailDto userDetailDto2 = new UserDetailDto();
        userDetailDto2.setName("kim");
        userDetailDto2.setAge(21);
        userDetailDto2.setNickname("lego");
        userDetailDto2.setGender("FeMale");

        User updateUser2 = userService.updateUserDetails(userDetailDto2, user2.getAccountId());

    }
}