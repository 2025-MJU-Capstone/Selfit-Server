package selfit.selfit.domain.body.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.dto.FaceFileDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.body.service.BodyService;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.service.UserService;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/body")
@RequiredArgsConstructor
public class BodyController {

    private final BodyService bodyService;

    /**
     * 로그인 한 사용자에 대한 얼굴 사진 업로드
     */
//    @PostMapping("/face")
//    public ApiResult<List<FaceFileDto>> uploadFace(@AuthenticationPrincipal CustomUserDetails customUserDetails,
//                           @RequestParam("file") List<MultipartFile> files) throws IOException {
//        Long userId = customUserDetails.getId();
//        List<FaceFileDto> dtos = bodyService.uploadFaceFiles(userId, files);
//        return ApiResult.ok(dtos);
//    }

    /**
     * 로그인 한 사용자에 대한 전신 체형 사진 업로드
     */
    @PostMapping("/shape")
    public void uploadBodyShape(@RequestParam("file") List<MultipartFile> files,
                           @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        Long userId = customUserDetails.getId();

    }

    /**
     * 로그인 한 사용자에 대한 체형 정보 저장
     */
    @PostMapping("/size")
    public ApiResult<BodySizeDto> saveSize(@RequestBody BodySizeDto bodySizeDto,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        bodyService.saveSize(userId, bodySizeDto);
        return ApiResult.ok("신체 정보 등록 완료", bodySizeDto);
    }
}
