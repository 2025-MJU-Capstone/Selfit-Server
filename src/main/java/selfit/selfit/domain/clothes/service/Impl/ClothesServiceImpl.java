package selfit.selfit.domain.clothes.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.clothes.dto.ClothesDto;
import selfit.selfit.domain.clothes.dto.ClothesType;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.clothes.repository.ClothesRepository;
import selfit.selfit.domain.clothes.service.ClothesService;
import selfit.selfit.domain.image.ImageFileStorageService;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;
import selfit.selfit.domain.wardrobe.repository.WardrobeRepository;

@Service
@RequiredArgsConstructor
public class ClothesServiceImpl implements ClothesService {

    @Autowired private final ClothesRepository clothesRepository;
    @Autowired private final ImageFileStorageService fileStorageService;
    @Autowired private final WardrobeRepository wardrobeRepository;

    /**
     *  담은 옷 저장
     */
    @Override
    public ClothesDto saveClothes(Long userId, ClothesType type, MultipartFile file){
        if(file == null){
            throw new IllegalArgumentException("한 장의 옷 사진을 업로드해야 합니다.");
        }

        // 파일 하나씩 저장, 절대경로 획득, 엔티티에 추가
        String filename = fileStorageService.store(file);
        String path = fileStorageService.getFilePath(filename);

        Wardrobe wardrobe = wardrobeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("옷장이 존재하지 않습니다."));

        // 엔티티 생성
        Clothes clothes = Clothes.builder()
                .type(type)
                .file_path(path)
                .build();

        clothes.setWardrobe(wardrobe);

        clothesRepository.save(clothes);

        ClothesDto clothesDto = new ClothesDto();
        clothesDto.setPath(clothes.getFile_path());
        clothesDto.setType(clothes.getType());

        return clothesDto;
    }

    /**
     *  담은 옷 삭제
     * */
    @Override
    public void deleteClothes(Long clothesId){
        Clothes clothes = findClothesById(clothesId);

        // 파일 시스템에서 삭제
        fileStorageService.delete(clothes.getFile_path());

        // DB에서 삭제
        clothesRepository.delete(clothes);
    }

    /**
     * 담은 옷 제공
     */
    @Override
    public ClothesDto provideClothes(Long clothesId){
        Clothes clothes = findClothesById(clothesId);

        ClothesDto clothesDto = new ClothesDto();
        clothesDto.setPath(clothes.getFile_path());
        clothesDto.setType(clothes.getType());

        return clothesDto;
    }

    public Clothes findClothesById(Long clothesId){
        return clothesRepository.findById(clothesId)
                .orElseThrow(() -> new IllegalArgumentException("해당 옷이 존재하지 않습니다. id: " + clothesId));
    }
}
