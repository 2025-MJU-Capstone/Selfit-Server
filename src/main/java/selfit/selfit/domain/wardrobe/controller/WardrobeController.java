package selfit.selfit.domain.wardrobe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.wardrobe.dto.WardrobeDto;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;
import selfit.selfit.domain.wardrobe.service.WardrobeService;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/wardrobe")
@RequiredArgsConstructor
public class WardrobeController {

    @Autowired private final WardrobeService wardrobeService;

    /**
     * 소장 의류 등록
     */
    @PostMapping("/photos")
    public ApiResult<List<String>> registerClothesFromWardrobe(@RequestParam("file") List<MultipartFile> files,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        List<String> paths = wardrobeService.saveClothes(userId, files);

        return ApiResult.ok("소장 의류 등록 완료", paths);
    }

    /**
     * 소장 의류 삭제
     */
    @DeleteMapping("/photos")
    public ApiResult<List<String>> deleteClothesFromWardrobe(@RequestParam("path") String path,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        List<String> remain = wardrobeService.deleteClothes(userId, path);

        return ApiResult.ok("소장 의류 삭제 완료", remain);
    }

    /**
     * 소장 의류 제공
     */
    @GetMapping("/photos/{index}")
    public ResponseEntity<Resource> provideClothesFromWardrobe(@PathVariable int index,
                                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        Long userId = customUserDetails.getId();
        Resource resource = wardrobeService.provideClothesResource(userId, index);

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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=photo")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

}
