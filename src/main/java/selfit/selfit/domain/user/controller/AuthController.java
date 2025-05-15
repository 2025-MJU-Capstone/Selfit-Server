package selfit.selfit.domain.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import selfit.selfit.domain.user.dto.LoginResponse;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.dto.UserLoginRequestDto;
import selfit.selfit.domain.user.dto.UserLoginResponseDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.service.UserService;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.exception.ErrorCode;
import selfit.selfit.global.security.jwt.TokenProvider;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final @Lazy UserService userService;

    // 회원가입
    @PostMapping("/save")
    public ApiResult<User> save(@Valid @RequestBody UserAccountDto userAccountDto){
        User user = userService.registerUser(userAccountDto);

        return ApiResult.ok("회원가입 성공", user);
    }

    // 로그인
    @PostMapping("/login")
    public ApiResult<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.getAccountId(),
                        userLoginRequestDto.getPassword()
                )
        );

        // 2. 인증 성공 시 JWT 생성
        String jwt = tokenProvider.createToken(authentication);

        // 3. Principal에서 User 정보 꺼내 DTO 생성
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findUser(userDetails.getUsername());

        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto(
                user.getId(),
                user.getAccountId(),
                user.getNickname(),
                user.getEmail(),
                jwt);

        return ApiResult.ok("로그인 인증 성공", userLoginResponseDto);

//        // springSecurity 사용 시
//        User user = userService.findUser(userLoginRequestDto.getAccountId());
//        UserLoginResponseDto userDto = new UserLoginResponseDto(
//                user.getId(), user.getAccountId(), user.getNickname(), user.getEmail());
//
//        return ApiResult.ok("로그인 인증 성공", userDto);
    }

    // 아이디 찾기
    @PostMapping("/find-accountId")
    public ApiResult<String> findId(@RequestParam String email){
        String accountId = userService.findAccountId(email);

        return ApiResult.ok("아이디: ", accountId);
    }

    // 임시 비밀번호 발급
    @PostMapping("/recover-password")
    public ApiResult<?> recoverPwd(@RequestParam String accountId, @RequestParam String email){
        String tempPwd = userService.recoverPassword(accountId, email);

        return ApiResult.ok("임시 비밀번호 발급 성공", tempPwd);
    }

    // 비밀번호 재설정
    @PostMapping("/reset-password")
    public ApiResult<?> resetPwd(@RequestParam String accountId, @RequestParam String password, @RequestParam String email) {
        userService.resetPassword(accountId, password, email);

        return ApiResult.ok("비밀번호 재설정 완료");
    }


    }
