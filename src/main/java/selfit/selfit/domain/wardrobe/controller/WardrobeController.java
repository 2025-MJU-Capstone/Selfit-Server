package selfit.selfit.domain.wardrobe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.wardrobe.dto.WardrobeDto;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;
import selfit.selfit.domain.wardrobe.service.WardrobeService;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

@RestController
@RequestMapping("/api/wardrobe")
@RequiredArgsConstructor
public class WardrobeController {

    @Autowired private final WardrobeService wardrobeService;

    /**
     * 소장 의류 등록
     */
    @PostMapping("/register")
    public ApiResult<WardrobeDto> registerClothesFromWardrobe(@RequestParam("id") Long clothesId,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        WardrobeDto wardrobeDto = wardrobeService.saveClothes(userId, clothesId);

        return ApiResult.ok("소장 의류 등록 완료", wardrobeDto);
    }
    /**
     * 소장 의류 삭제
     */
    @PostMapping("/delete")
    public ApiResult<WardrobeDto> deleteClothesFromWardrobe(@RequestParam("id") Long clothesId,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        WardrobeDto wardrobeDto = wardrobeService.removeClothes(userId, clothesId);

        return ApiResult.ok("소장 의류 삭제 완료", wardrobeDto);
    }

    /**
     * 소장 의류 제공
     */
    @PostMapping("/provide")
    public ApiResult<WardrobeDto> provideClothesFromWardrobe(@RequestParam("id") Long clothesId,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        WardrobeDto wardrobeDto = wardrobeService.provideClothes(userId, clothesId);

        return ApiResult.ok("소장 의류 제공 완료", wardrobeDto);
    }

}
