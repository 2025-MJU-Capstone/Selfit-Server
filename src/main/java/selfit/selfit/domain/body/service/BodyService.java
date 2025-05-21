package selfit.selfit.domain.body.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.dto.FaceFileDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.user.entity.User;

import java.util.List;

@Service
public interface BodyService {
    Body saveSize(Long userId, BodySizeDto bodySizeDto);
//    List<FaceFileDto> uploadFaceFiles(Long userId, List<MultipartFile> files);
}
