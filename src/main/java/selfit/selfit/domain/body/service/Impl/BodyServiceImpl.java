package selfit.selfit.domain.body.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.dto.FaceFileDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.body.repository.BodyRepository;
import selfit.selfit.domain.body.service.BodyService;
import selfit.selfit.domain.image.ImageFileStorageService;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BodyServiceImpl implements BodyService {

    private final BodyRepository bodyRepository;
    private final UserRepository userRepository;
    private final ImageFileStorageService imageFileStorageService;

    public User getUserByUserId(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    /**
     *  신체 정보 저장
     */
    @Override
    public Body saveSize(Long userId, BodySizeDto bodySizeDto) {
        User user = getUserByUserId(userId);

        Body body = bodyRepository.findByUser(user)
                .orElseGet(() -> {
                    Body b = Body.builder()
                            .user(user)         // user_id 매핑
                            .build();
                    return b;
                });

        body.setHeight(bodySizeDto.getHeight());
        body.setWeight(bodySizeDto.getWeight());
        body.setWaist(bodySizeDto.getWaist());

        return bodyRepository.save(body);
    }
//
//    @Override
//    public List<FaceFileDto> uploadFaceFiles(Long userId, List<MultipartFile> files) {
//        if(files==null||files.isEmpty()){
//            throw new IllegalArgumentException("최소 한 장의 사진을 업로드해야 합니다.");
//        }
//
//        User user = getUserByUserId(userId);
//
//        Body body = bodyRepository.findByUser(user)
//                .orElseThrow(() -> new IllegalArgumentException("신체 정보가 없습니다."));
//
//        List<FaceFileDto> result = files.stream().map(file->{
//            try{
//                String filename = imageFileStorageService.store(file);
//                String path = imageFileStorageService.getFilePath(filename);
//                // entity에 경로 추가
//                body.addFaceFile(path);
//                return new FaceFileDto(filename, path);
//            }catch (Exception e){
//                throw new RuntimeException("파일 저장 실패: " + file.getOriginalFilename(), e);
//            }
//        }).collect(Collectors.toList());
//
//        bodyRepository.save(body);
//        return result;
//    }

}
