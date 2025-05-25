package selfit.selfit.domain.clothes.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.clothes.dto.ClothesDto;
import selfit.selfit.domain.clothes.dto.ClothesType;
import selfit.selfit.domain.clothes.entity.Clothes;

@Service
public interface ClothesService {
    ClothesDto saveClothes(Long userId, ClothesType type, MultipartFile file);
    void deleteClothes(Long clothesId);
    ClothesDto provideClothes(Long clothesId);
}
