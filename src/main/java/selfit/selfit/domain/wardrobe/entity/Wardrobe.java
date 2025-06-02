package selfit.selfit.domain.wardrobe.entity;

import jakarta.persistence.*;
import lombok.*;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.user.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "Wardrobe") // 소장 의류
public class Wardrobe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date", nullable = false)
    private Date create_date;

    @Column(name = "update_date", nullable = false)
    private Date update_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "path", nullable = false)
    private String path;

    @Builder
    public Wardrobe(User user, String path) {
        this.user = user;
        this.path = path;
        this.create_date = new Date();
        this.update_date = new Date();
    }
}
