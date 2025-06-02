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
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClothesServiceImpl implements ClothesService {

    @Autowired private final ClothesRepository clothesRepository;
    @Autowired private final ImageFileStorageService imageFileStorageService;
    @Autowired private final UserRepository userRepository;

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

        User user = userRepository.findById(userId).orElseThrow();

        // 엔티티 생성
        Clothes clothes = Clothes.builder()
                .type(type)
                .path(path)
                .build();

        clothes.setUser(user);

        clothesRepository.save(clothes);

        return path;
    }

    /**
     *  담은 옷 삭제
     * */
    @Override
    public List<String> deleteClothes(Long userId, int index) {
        List<String> paths = listClothesPathsByUser(userId);
        if (index < 0 || index >= paths.size()) {
            throw new IllegalArgumentException("삭제할 옷을 선택하세요.");
        }
        String deletePath = paths.get(index);
        deleteClothesPath(deletePath);

        return listClothesPathsByUser(userId);
    }

    private void deleteClothesPath(String path) {
        Clothes clothes = clothesRepository.findByPath(path)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지 경로입니다: " + path));
        clothesRepository.delete(clothes);
        imageFileStorageService.delete(path);
    }

    private List<String> listClothesPathsByUser(Long userId){
        return clothesRepository.findByUserId(userId).stream()
                .map(Clothes::getPath)
                .collect(Collectors.toList());
    }

    /**
     * 담은 옷 제공
     */
    @Override
    public Resource provideClothes(Long userId, int index) throws MalformedURLException {
        List<String> clothesPathList = listClothesPathsByUser(userId);
        if (index < 0 || index >= clothesPathList.size()) {
            throw new IllegalArgumentException("사진을 선택하세요");
        }
        String path = clothesPathList.get(index);
        Path file = Path.of(path);
        UrlResource resource = new UrlResource(file.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        }
        throw new IllegalArgumentException("파일을 찾을 수 없거나 읽을 수 없습니다: " + path);
    }

    @Override
    public List<String> findClothesAll(Long userId) {
        return listClothesPathsByUser(userId);
    }
}
