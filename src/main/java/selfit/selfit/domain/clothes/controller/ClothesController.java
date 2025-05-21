package selfit.selfit.domain.clothes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
//import selfit.selfit.domain.clothes.dto.ClothesDto;
import selfit.selfit.domain.clothes.dto.ClothesPhotoFileDto;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/clothes")
@RequiredArgsConstructor
public class ClothesController {


    /**
     *  옷 담기 등록
     */
//    @PostMapping("/register-clothes")
//    public ApiResult<ClothesPhotoFileDto> registerClothes(@AuthenticationPrincipal CustomUserDetails customUserDetails,
//                                                          @RequestParam("file") List<MultipartFile> files,
//                                                          @RequestParam ClothesPhotoFileDto clothesPhotoFileDto) throws IOException {
//
//
//    }
}
