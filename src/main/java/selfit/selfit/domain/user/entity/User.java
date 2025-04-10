package selfit.selfit.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String email;
    private String accountId;
    private String password;
    private String nickname;
    private String gender; //M, F로 구분.

    private LocalDateTime create_date;
    private LocalDateTime update_date;

    @Builder
    public User(String name, int age, String email, String accountId, String password, String nickname, String gender) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.accountId = accountId;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.create_date = LocalDateTime.now();
        this.update_date = LocalDateTime.now();
    }

}
