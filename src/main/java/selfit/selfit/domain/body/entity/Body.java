package selfit.selfit.domain.body.entity;

import jakarta.persistence.*;
import lombok.*;
import selfit.selfit.domain.user.entity.User;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "Body")
public class Body {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String face;
    private String height;
    private String weight;
    private String waist;
    private String photo;

    private Date create_date;
    private Date update_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Body(String face, String height, String weight, String waist, String photo, User user) {
        this.face = face;
        this.height = height;
        this.weight = weight;
        this.waist = waist;
        this.photo = photo;
        this.user = user;
        this.create_date = new Date();
        this.update_date = new Date();
    }
}
