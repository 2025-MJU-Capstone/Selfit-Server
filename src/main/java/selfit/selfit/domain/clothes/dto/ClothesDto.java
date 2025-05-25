package selfit.selfit.domain.clothes.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClothesDto {
    private String path;
    private ClothesType type;
}
