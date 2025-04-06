package selfit.selfit.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailDto {
    //필요한 요소 있으면 추가해서 사용.
    private String name;
    private int age;
    private String email;
    private String nickname;
    private String gender;

    @Builder
    public UserDetailDto(String name, String email, String nickname, String gender, int age) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
    }
}
