package selfit.selfit.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import selfit.selfit.domain.user.dto.UserDetailDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.service.UserService;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 개인정보 저장", description = "주어진 유저 개인정보를 저장합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 저장됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/detail")
    public ApiResult<UserDetailDto> registerDetailInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              @RequestBody UserDetailDto userDetailDto) {
        // 로그인 한 사용자는 jwt를 통해 securityContext에 저장되었으므로
        // 안에 있는 accountId를 꺼내서 사용하면 된다.
        // 위 코드는 @CurrentUser 대체 가능
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String accountId = customUserDetails.getUsername();
        userService.updateUserDetails(userDetailDto, accountId);

        return ApiResult.ok("개인정보 등록 완료", userDetailDto);
    }


}

