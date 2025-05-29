package selfit.selfit.domain.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.dto.UserLoginRequestDto;
import selfit.selfit.domain.user.dto.UserLoginResponseDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.service.UserService;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.security.jwt.TokenProvider;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final @Lazy UserService userService;

    @Operation(summary = "유저 회원가입", description = "주어진 정보를 토대로 회원가입 합니다.",
            responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 저장됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/save")
    public ApiResult<String> save(@Valid @RequestBody UserAccountDto userAccountDto){
        userService.registerUser(userAccountDto);
        return ApiResult.ok(userAccountDto.getAccountId() + "님, 회원가입 성공");
    }

    @Operation(summary = "유저 로그인", description = "유저가 로그인 합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 로긍니됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/login")
    public ApiResult<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto){
        if(userLoginRequestDto == null){
            throw new IllegalArgumentException("로그인에 필요한 정보가 없습니다.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.getAccountId(),
                        userLoginRequestDto.getPassword()
                )
        );

        // 인증 정보(SecurityContext) 저장 (선택 사항)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. 인증 성공 시 JWT 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        // 3. Principal에서 User 정보 꺼내 DTO 생성
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findUser(userDetails.getUsername());

        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto(
                user.getId(),
                user.getAccountId(),
                user.getNickname(),
                user.getEmail(),
                accessToken,
                refreshToken);

        return ApiResult.ok("로그인 인증 성공", userLoginResponseDto);
    }

    @Operation(summary = "유저 아이디 찾기", description = "이메일 정보로 아이디를 찾습니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 찾음"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/find-accountId")
    public ApiResult<String> findId(@RequestParam String email){
        String accountId = userService.findAccountId(email);

        return ApiResult.ok("아이디: ", accountId);
    }

    @Operation(summary = "유저 임시 비밀번호 발급", description = "유저 비밀번호 찾기 중 임시 비밀번호 발급",
    responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 발급"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/recover-password")
    public ApiResult<String> recoverPwd(@RequestParam String accountId, @RequestParam String email){
        String tempPwd = userService.recoverPassword(accountId, email);

        return ApiResult.ok("임시 비밀번호 발급 성공", tempPwd);
    }

    @Operation(summary = "유저 비밀번호 재설정", description = "유저 비밀번호 찾기 중 비밀번호 재설정",
    responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 변경"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/reset-password")
    public ApiResult<String> resetPwd(@RequestParam String accountId, @RequestParam String password, @RequestParam String email, @RequestParam String newPwd) {
        userService.resetPassword(accountId, password, email, newPwd);

        return ApiResult.ok("비밀번호 재설정 완료");
    }


}
