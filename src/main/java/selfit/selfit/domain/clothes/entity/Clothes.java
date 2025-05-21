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
    @JoinColumn(nullable = false)
    private ClothesType type;

    @ElementCollection
    @CollectionTable(
            name = "clothes_photos",
            joinColumns = @JoinColumn(name = "clothes_id")
    )
    @Column(name = "photo_file", nullable = false)
    private List<String> photoFiles = new ArrayList<>(); // 사진 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wardrobe_id")
    private Wardrobe wardrobe;

    @Builder
    public Clothes(Wardrobe wardrobe, ClothesType type) {
        this.wardrobe = wardrobe;
        this.type = type;
        this.create_date = new Date();
        this.update_date = new Date();
    }

    public void addPhotoFile(String path){
        photoFiles.add(path);
        this.update_date = new Date();
    }
}