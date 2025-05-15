package selfit.selfit.domain.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAccountDto {
    // 계정 생성 시 사용하는 DTO

    @NotBlank(message = "아이디를 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$"
            , message = "특수문자는 사용이 불가합니다.")
    @Size(min = 8, message = "영문과 숫자를 사용하여 8자리 이상 입력해주세요.")
    private String accountId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]+$",
    message = "영문과 숫자 특수문자를 사용해 주세요.")
    @Size(min = 8, message = "8자리 이상 입력해주세요.")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Builder
    public UserAccountDto(String accountId, String password, String email) {
        this.accountId = accountId;
        this.password = password;
        this.email = email;
    }
}
