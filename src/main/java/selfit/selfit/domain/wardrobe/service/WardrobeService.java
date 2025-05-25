package selfit.selfit.domain.wardrobe.service;

import org.springframework.stereotype.Service;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.wardrobe.dto.WardrobeDto;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;

@Service
public interface WardrobeService {
    WardrobeDto saveClothes(Long userId, Long clothesId);
    WardrobeDto removeClothes(Long userId, Long clothesId);
    WardrobeDto provideClothes(Long userId, Long clothesId);

}
