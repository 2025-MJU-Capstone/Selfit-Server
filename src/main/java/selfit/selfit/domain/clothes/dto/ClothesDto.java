package selfit.selfit.domain.clothes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClothesDto {
    private String photo_file;
    private String category;
    private String type;

    @Builder
    public ClothesDto(String photo_file, String category, String type) {
        this.photo_file = photo_file;
        this.category = category;
        this.type = type;
    }
}
