package selfit.selfit.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponseDto {
    // 로그인 응답 DTO
    private Long userId;
    private String accountId;
    private String nickname;
    private String email;
    private String accessToken; // access token
    private String refreshToken; // refresh token
}
