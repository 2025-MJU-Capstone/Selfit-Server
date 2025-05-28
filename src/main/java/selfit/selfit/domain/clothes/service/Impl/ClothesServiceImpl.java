package selfit.selfit.domain.clothes.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClothesServiceImpl implements ClothesService {

    @Autowired private final ClothesRepository clothesRepository;
    @Autowired private final ImageFileStorageService imageFileStorageService;
    @Autowired private final WardrobeRepository wardrobeRepository;

    /**
     *  담은 옷 저장
     */
    @Override
    public String saveClothes(Long userId, ClothesType type, MultipartFile file){
        if(file == null){
            throw new IllegalArgumentException("한 장의 옷 사진을 업로드해야 합니다.");
        }

        // 파일 하나씩 저장, 절대경로 획득, 엔티티에 추가
        String filename = imageFileStorageService.store(file);
        String path = imageFileStorageService.getFilePath(filename);

        Wardrobe wardrobe = wardrobeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("옷장이 존재하지 않습니다."));

        // 엔티티 생성
        Clothes clothes = Clothes.builder()
                .type(type)
                .path(path)
                .build();

        clothes.setWardrobe(wardrobe);

        clothesRepository.save(clothes);

        return path;
    }

    /**
     *  담은 옷 삭제
     * */
    @Override
    public String deleteClothes(String path) {
        Clothes clothes = clothesRepository.findByPath(path)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지 경로입니다: " + path));

        clothesRepository.delete(clothes);

        imageFileStorageService.delete(path);

        return path;
    }

    /**
     * 담은 옷 제공
     */
    @Override
    public Resource provideClothes(String filePath) {
        Clothes clothes = clothesRepository.findByPath(filePath)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지 경로입니다: " + filePath));

        Path path = Path.of(clothes.getPath());
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new IllegalArgumentException("이미지 파일을 읽을 수 없습니다: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("잘못된 파일 경로입니다: " + filePath, e);
        }
    }
}
