package selfit.selfit.domain.fitted.service;

import org.springframework.stereotype.Service;
import selfit.selfit.domain.fitted.dto.FittedImageDto;

import java.util.List;

@Service
public interface FittedImageService {
    FittedImageDto fitting3D (Long userId, String clothPath);
    List<FittedImageDto> fittingList (Long userId);
}
