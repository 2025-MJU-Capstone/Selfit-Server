package selfit.selfit.domain.body.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.body.repository.BodyRepository;
import selfit.selfit.domain.body.service.BodyService;
import selfit.selfit.domain.image.ImageFileStorageService;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;

import java.util.Date;
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
                .orElseThrow(() -> new IllegalArgumentException("신체 정보가 없습니다."));

        setSize(body, bodySizeDto);
        user.setBody(body);
        userRepository.save(user);
        return bodyRepository.save(body);
    }

    public void setSize(Body body, BodySizeDto dto){
        body.setHeight(dto.getHeight());
        body.setWeight(dto.getWeight());
        body.setWaist(dto.getWaist());
        body.setLeg(dto.getLeg());
        body.setShoulder(dto.getShoulder());
        body.setPelvis(dto.getPelvis());
        body.setChest(dto.getChest());
        body.setUpdate_date(new Date());
    }

    @Override
    public List<String> uploadFullBody(User user, List<MultipartFile> files) {
        if (files == null || files.size() != 4) {
            throw new IllegalArgumentException("전신 사진은 정확히 4장 업로드해야 합니다.");
        }
        Body body = bodyRepository.findByUser(user)
                .orElseGet(() -> Body.builder().user(user).build());

        List<String> storedPaths = files.stream()
                .map(file -> {
                    String filename = imageFileStorageService.store(file);
                    return imageFileStorageService.getFilePath(filename);
                })
                .collect(Collectors.toList());

        setFullBodyPhotos(body, storedPaths);
        bodyRepository.save(body);
        return storedPaths;
    }

    public void setFullBodyPhotos(Body body, List<String> paths){
        body.getFullBodyPhotos().clear();
        body.getFullBodyPhotos().addAll(paths);
        body.setUpdate_date(new Date());
    }

    /**
     * 얼굴 사진 최대 9장 업로드 로직
     */
    @Override
    public List<String> uploadFace(User user, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("최소 1장의 얼굴 사진을 업로드해야 합니다.");
        }
        if (files.size() > 9) {
            throw new IllegalArgumentException("얼굴 사진은 최대 9장까지 업로드 가능합니다.");
        }
        Body body = bodyRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("신체 정보가 존재하지 않습니다."));

        List<String> storedPaths = files.stream()
                .map(file -> {
                    String filename = imageFileStorageService.store(file);
                    return imageFileStorageService.getFilePath(filename);
                })
                .collect(Collectors.toList());

        setFacePhotos(body, storedPaths);
        bodyRepository.save(body);
        return storedPaths;
    }

    public void setFacePhotos(Body body, List<String> paths) {
        body.getFacePhotos().clear();
        body.getFacePhotos().addAll(paths);
        body.setUpdate_date(new Date());
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
