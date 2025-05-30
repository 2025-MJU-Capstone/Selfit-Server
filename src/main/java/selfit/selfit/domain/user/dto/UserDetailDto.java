package selfit.selfit.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailDto {
    // 개인정보 등록 시 사용하는 DTO

    private String name;
    private int age;
    private String nickname;
    private String gender;

    @Builder
    public UserDetailDto(String name, String nickname, String gender, int age) {
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
    }

}
