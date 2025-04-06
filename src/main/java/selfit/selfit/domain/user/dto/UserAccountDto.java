package selfit.selfit.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAccountDto {
    private String account_id;
    private String account_password;

    @Builder
    public UserAccountDto(String account_id, String account_password) {
        this.account_id = account_id;
        this.account_password = account_password;
    }
}
