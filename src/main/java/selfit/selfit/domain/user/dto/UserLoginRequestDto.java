package selfit.selfit.domain.user.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    // 로그인 요청 DTO
    private String accountId;
    private String password;
}
