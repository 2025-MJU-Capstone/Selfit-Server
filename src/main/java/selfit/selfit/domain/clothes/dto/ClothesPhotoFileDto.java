package selfit.selfit.domain.clothes.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClothesPhotoFileDto {
    private String filePath;
    private String fileName;
    private ClothesType type;
}
