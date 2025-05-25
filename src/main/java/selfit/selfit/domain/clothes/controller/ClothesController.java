package selfit.selfit.domain.clothes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import selfit.selfit.domain.clothes.dto.ClothesDto;
import selfit.selfit.domain.clothes.dto.ClothesDto;
import selfit.selfit.domain.clothes.dto.ClothesType;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.clothes.service.ClothesService;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

import java.io.IOException;

@RestController
@RequestMapping("/api/clothes")
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;

    /**
     *  옷 담기 등록
     */
    @PostMapping("/register")
    public ApiResult<ClothesDto> registerClothes(@RequestParam("type") ClothesType type,
                                                 @RequestParam("file") MultipartFile file,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException{

        Long userId = customUserDetails.getId();
        ClothesDto clothesDto = clothesService.saveClothes(userId, type, file);
        return ApiResult.ok("담은 옷에 등록", clothesDto);
    }

    /**
     *  담은 옷 삭제
     */
    @PostMapping("/delete")
    public ApiResult<?> deleteClothes(@RequestParam("id") Long clothesId) throws IOException{
        clothesService.deleteClothes(clothesId);
        return ApiResult.ok("담은 옷 삭제", clothesId);
    }

    /**
     *  담은 옷 제공
     */
    @PostMapping("/get")
    public ApiResult<ClothesDto> getClothes(@RequestParam("id") Long clothesId) throws IOException{
        return ApiResult.ok("담은 옷 제공 성공", clothesService.provideClothes(clothesId));
    }
}
