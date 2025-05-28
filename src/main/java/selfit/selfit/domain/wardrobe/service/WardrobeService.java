package selfit.selfit.domain.wardrobe.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.wardrobe.dto.WardrobeDto;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;

import java.net.MalformedURLException;
import java.util.List;

@Service
public interface WardrobeService {
    List<String> saveClothes(Long userId, List<MultipartFile> files);
    List<String> deleteClothes(Long userId, String photoPath);
    String provideClothes(Long userId, int index);
    Resource provideClothesResource(Long userId, int index) throws MalformedURLException;
}
