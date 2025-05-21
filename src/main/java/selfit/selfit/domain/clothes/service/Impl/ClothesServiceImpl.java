package selfit.selfit.domain.clothes.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selfit.selfit.domain.clothes.dto.ClothesPhotoFileDto;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.clothes.repository.ClothesRepository;
import selfit.selfit.domain.clothes.service.ClothesService;

@Service
@RequiredArgsConstructor
public class ClothesServiceImpl implements ClothesService {

    @Autowired private final ClothesRepository clothesRepository;


//    @Override
//    public Clothes saveClothes(ClothesPhotoFileDto dto){
//        Clothes clothes =
//    }
}
