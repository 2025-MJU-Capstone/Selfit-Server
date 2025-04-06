package selfit.selfit.domain.clothes.entity;

import jakarta.persistence.*;
import lombok.*;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "Clothes")
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo_file;
    private String category;
    private String type;

    private Date create_date;
    private Date update_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wardrobe_id")
    private Wardrobe wardrobe;

    @Builder
    public Clothes(String photo_file, String category, String type) {
        this.photo_file = photo_file;
        this.category = category;
        this.type = type;
        this.create_date = new Date();
        this.update_date = new Date();
    }
}
