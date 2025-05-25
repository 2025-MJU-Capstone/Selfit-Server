package selfit.selfit.domain.clothes.entity;

import jakarta.persistence.*;
import lombok.*;
import selfit.selfit.domain.clothes.dto.ClothesType;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "Clothes")
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date", nullable = false)
    private Date create_date;

    @Column(name = "update_date", nullable = false)
    private Date update_date;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "type", nullable = false)
    private ClothesType type;

    @Column(name = "file_path", nullable = false)
    private String file_path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wardrobe_id", nullable = false)
    private Wardrobe wardrobe;

    @Builder
    public Clothes(String file_path, ClothesType type) {
        this.file_path = file_path;
        this.type = type;
        this.create_date = new Date();
        this.update_date = new Date();
    }
}