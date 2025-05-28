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
@Table(name = "Wardrobe")
public class Wardrobe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date", nullable = false)
    private Date create_date;

    @Column(name = "update_date", nullable = false)
    private Date update_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ElementCollection
    @CollectionTable(name = "clothes_photos",
            joinColumns = @JoinColumn(name = "wardrobe_id"))
    @Column(name = "clothes_path", nullable = false)
    private List<String> clothesPhotos = new ArrayList<>();

    @Builder
    public Wardrobe(User user) {
        this.user = user;
        this.create_date = new Date();
        this.update_date = new Date();
    }
}
