package selfit.selfit.domain.clothes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/clothes")
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;

    /**
     *  옷 담기 등록
     */
    @PostMapping("/upload")
    public ApiResult<String> registerClothes(@RequestParam("type") ClothesType type,
                                                 @RequestParam("file") MultipartFile file,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException{

        Long userId = customUserDetails.getId();
        String path = clothesService.saveClothes(userId, type, file);
        return ApiResult.ok("담은 옷 등록", path);
    }

    /**
     *  담은 옷 삭제
     */
    @DeleteMapping("/upload/{index}")
    public ApiResult<List<String>> deleteClothes(@PathVariable int index,
                                           @AuthenticationPrincipal CustomUserDetails details) throws IOException{
        Long userId = details.getId();
        List<String> remain = clothesService.deleteClothes(userId, index);
        return ApiResult.ok("선택한 사진 삭제 성공", remain);
    }

    /**
     *  담은 옷 제공
     */
    @GetMapping("/upload/{index}")
    public ResponseEntity<Resource> provideClothes(@PathVariable int index,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        Long userId = userDetails.getId();
        Resource resource = clothesService.provideClothes(userId, index);
        // Content-Type 설정
        String contentType;
        try {
            contentType = Files.probeContentType(Path.of(resource.getURI()));
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
        } catch (Exception e) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clothes_photo"+index)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
