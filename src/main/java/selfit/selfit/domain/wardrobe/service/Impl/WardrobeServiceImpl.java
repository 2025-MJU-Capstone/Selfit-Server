package selfit.selfit.domain.wardrobe.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.clothes.repository.ClothesRepository;
import selfit.selfit.domain.image.ImageFileStorageService;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;
import selfit.selfit.domain.wardrobe.repository.WardrobeRepository;
import selfit.selfit.domain.wardrobe.service.WardrobeService;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WardrobeServiceImpl implements WardrobeService {

    @Autowired private final WardrobeRepository wardrobeRepository;
    @Autowired private final UserRepository userRepository;
    @Autowired private final ImageFileStorageService imageFileStorageService;

    /**
     * 소장 의류 등록
     */
    @Override
    public String saveClothes(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("최소 1장의 옷 사진을 업로드해야 합니다.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 파일 저장 및 절대 경로 획득
        String filename = imageFileStorageService.store(file);
        String path = imageFileStorageService.getFilePath(filename);


        Wardrobe wardrobe = Wardrobe.builder()
                .user(user)
                .path(path)
                .build();

        wardrobe.setUpdate_date(new Date());
        wardrobeRepository.save(wardrobe);

        return path;
    }

    /**
     * 소장 의류 삭제
     */
    @Override
    public List<String> deleteClothes(Long userId, int index) {
        List<String> paths = listClothesPathsByUserFromWardrobe(userId);
        if (index < 0 || index >= paths.size()) {
            throw new IllegalArgumentException("삭제할 옷을 선택하세요.");
        }
        String deletePath = paths.get(index);

        // 파일 시스템에서 삭제
        imageFileStorageService.delete(deletePath);

        // 소장 의류 탐색
        Wardrobe wardrobe = wardrobeRepository.findByPath(deletePath)
                .orElseThrow();

        // 소장 의류 제거
        wardrobeRepository.delete(wardrobe);
        wardrobe.setUpdate_date(new Date());

        return listClothesPathsByUserFromWardrobe(userId);
    }


    public List<String> listClothesPathsByUserFromWardrobe(Long userId){
        return wardrobeRepository.findByUserId(userId).stream()
                .map(Wardrobe::getPath)
                .collect(Collectors.toList());
    }

    /**
     * 소장 의류 경로 제공
     */
    private String provideClothes(Long userId, int index) {
        List<String> clothesPathListFromWardrobe = listClothesPathsByUserFromWardrobe(userId);
        if (index < 0 || index >= clothesPathListFromWardrobe.size()) {
            throw new IllegalArgumentException("올바른 사진 인덱스를 선택하세요: " + index);
        }
        return clothesPathListFromWardrobe.get(index);
    }

    /**
     *  소장 의류 사진 Resource로 제공
     */
    @Override
    public Resource provideClothesResource(Long userId, int index) throws MalformedURLException {
        // 1) 내부에서 제공할 파일 경로 조회
        String path = provideClothes(userId, index);
        // 2) Path -> UrlResource 변환
        Path filePath = Path.of(path);
        UrlResource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IllegalArgumentException("파일을 읽을 수 없습니다: " + path);
        }
    }

    @Override
    public List<String> findWardrobeAll(Long userId) {
        return listClothesPathsByUserFromWardrobe(userId);
    }

}
