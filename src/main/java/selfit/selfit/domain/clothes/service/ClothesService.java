package selfit.selfit.domain.clothes.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.clothes.dto.ClothesDto;
import selfit.selfit.domain.clothes.dto.ClothesType;
import selfit.selfit.domain.clothes.entity.Clothes;

@Service
public interface ClothesService {
    String saveClothes(Long userId, ClothesType type, MultipartFile file);
    String deleteClothes(String path);
    Resource provideClothes(String path);
}
