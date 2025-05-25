package selfit.selfit.domain.wardrobe.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.clothes.repository.ClothesRepository;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.wardrobe.dto.WardrobeDto;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;
import selfit.selfit.domain.wardrobe.repository.WardrobeRepository;
import selfit.selfit.domain.wardrobe.service.WardrobeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WardrobeServiceImpl implements WardrobeService {

    @Autowired private final WardrobeRepository wardrobeRepository;
    @Autowired private final ClothesRepository clothesRepository;
    @Autowired private final UserRepository userRepository;

    /**
     * 소장 의류 등록
     */
    @Override
    public WardrobeDto saveClothes(Long userId, Long clothesId) {
        Clothes clothes = findClothesById(clothesId);

        Wardrobe wardrobe = wardrobeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("소장 의류가  존재하지 않습니다."));

        clothes.setWardrobe(wardrobe);

        wardrobe.getClothesList().add(clothes);
        wardrobe.setUpdate_date(new Date());
        wardrobeRepository.save(wardrobe);

        WardrobeDto wardrobeDto = new WardrobeDto();
        wardrobeDto.setC(clothes);

        return wardrobeDto;
    }

    /**
     * 소장 의류 삭제
     */
    @Override
    public WardrobeDto removeClothes(Long userId, Long clothesId) {
        Clothes clothes = findClothesById(clothesId);
        Wardrobe wardrobe = findWardrobeByUserId(userId);

        // 옷이 옷장에 없으면
        if(!wardrobe.getClothesList().contains(clothes)){
            throw new IllegalArgumentException("소장 의류에 등록된 옷이 아닙니다." + clothesId);
        }

        wardrobe.getClothesList().remove(clothes);
        wardrobe.setUpdate_date(new Date());

        wardrobeRepository.save(wardrobe);

        WardrobeDto wardrobeDto = new WardrobeDto();
        wardrobeDto.setC(clothes);

        return wardrobeDto;
    }

    /**
     * 소장 의류 제공
     */
    @Override
    public WardrobeDto provideClothes(Long userId, Long clothesId) {
        Wardrobe wardrobe = findWardrobeByUserId(userId);
        WardrobeDto wardrobeDto = new WardrobeDto();

        for (Clothes c : wardrobe.getClothesList()) {
            if(Objects.equals(clothesId, c.getId())){
                wardrobeDto.setC(c);
            }
        }
        return wardrobeDto;
    }

    public Wardrobe findWardrobeByUserId(Long userId){
        return wardrobeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("소장 의류가 존재하지 않습니다."));
    }

    public Clothes findClothesById(Long clothesId){
        return clothesRepository.findById(clothesId)
                .orElseThrow(() -> new IllegalArgumentException("소장 의류가 존재하지 않습니다."));
    }
}
