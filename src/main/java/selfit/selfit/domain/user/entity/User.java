package selfit.selfit.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String account_id;
    private String account_password;
    private String nickname;
    private String gender; //M, F로 구분.

    private Date create_date;
    private Date update_date;

    @Builder
    public User(String name, int age, String email, String account_id, String account_password, String nickname, String gender) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.account_id = account_id;
        this.account_password = account_password;
        this.nickname = nickname;
        this.gender = gender;
        this.create_date = new Date();
        this.update_date = new Date();
    }
}
