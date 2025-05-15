package selfit.selfit.domain.body.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.body.dto.BodySizeDto;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.body.service.BodyService;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.service.UserService;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

import java.io.IOException;

@RestController
@RequestMapping("api/body")
@RequiredArgsConstructor
public class BodyController {

    private final BodyService bodyService;


    /**
     * 로그인 한 사용자에 대한 얼굴 사진 업로드
     */
    @PostMapping("/face")
    public void uploadFace(@RequestParam("file") MultipartFile file,
                           @AuthenticationPrincipal CustomUserDetails principal) throws IOException {
        Long userId = principal.getId();

    }

    /**
     * 로그인 한 사용자에 대한 전신 체형 사진 업로드
     */
    @PostMapping("/shape")
    public void uploadBodyShape(@RequestParam("file") MultipartFile file,
                           @AuthenticationPrincipal CustomUserDetails principal) throws IOException {
        Long userId = principal.getId();

    }

    /**
     * 로그인 한 사용자에 대한 체형 정보 저장
     */
    @PostMapping("/size")
    public ApiResult<Body> saveSize(@RequestBody BodySizeDto bodySizeDto,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        Body result = bodyService.saveSize(userId, bodySizeDto);
        return ApiResult.ok("신체 정보", result);
    }
}
