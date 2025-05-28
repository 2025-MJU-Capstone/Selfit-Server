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
    @DeleteMapping("/upload")
    public ApiResult<String> deleteClothes(@RequestParam("path") String filePath) throws IOException{
        String path = clothesService.deleteClothes(filePath);
        return ApiResult.ok("담은 옷 삭제", path);
    }

    /**
     *  담은 옷 제공 (프론트에서 선택한
     */
    @GetMapping("/upload")
    public ResponseEntity<Resource> exportPhoto(@RequestParam("path") String filePath) {
        Resource resource = clothesService.provideClothes(filePath);
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clothes_photo")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
