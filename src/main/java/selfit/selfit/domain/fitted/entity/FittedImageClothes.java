package selfit.selfit.domain.fitted.entity;

import jakarta.persistence.*;
import lombok.*;
import selfit.selfit.domain.clothes.entity.Clothes;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "FittedImageClothes")
public class FittedImageClothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitted_image_id")
    private FittedImage fittedImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id")
    private Clothes clothes;

    @Builder
    public FittedImageClothes(FittedImage fittedImage, Clothes clothes) {
        this.fittedImage = fittedImage;
        this.clothes = clothes;
    }
}
