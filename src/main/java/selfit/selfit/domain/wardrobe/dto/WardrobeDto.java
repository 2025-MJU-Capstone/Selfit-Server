package selfit.selfit.domain.wardrobe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import selfit.selfit.domain.clothes.entity.Clothes;

@Getter
@Setter
@NoArgsConstructor
public class WardrobeDto {
    private Clothes c;
}
