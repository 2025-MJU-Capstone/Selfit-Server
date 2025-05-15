package selfit.selfit.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    // 로그인 성공 시 최종 응답 DTO
    // jwt 미사용시 필요 X
    private String accessToken;
    private UserLoginResponseDto userLoginResponseDto;
}
