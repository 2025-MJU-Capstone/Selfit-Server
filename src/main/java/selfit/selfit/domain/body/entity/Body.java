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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String height;
    private String weight;
    private String waist;

    @Column(name="create_date", nullable=false)
    private Date create_date;

    @Column(name="update_date", nullable=false)
    private Date update_date;

    @Builder
    public Body(User user, String height, String weight, String waist) {
        this.user       = user;
        this.height     = height;
        this.weight     = weight;
        this.waist      = waist;
        this.create_date = new Date();
        this.update_date = new Date();
    }
}
