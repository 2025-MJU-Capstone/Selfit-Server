package selfit.selfit.domain.wardrobe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "소장 의류 등록", description = "사용자가 소유한 옷의 이미지를 등록하고, 등록한 옷들의 경로를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "등록 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 필요")
            }
    )
    @PostMapping("/photos")
    public ApiResult<String> registerClothesFromWardrobe(@RequestParam("file") MultipartFile file,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        String path = wardrobeService.saveClothes(userId, file);

        return ApiResult.ok("소장 의류 등록 완료", path);
    }

    @Operation(summary = "소장 의류 삭제", description = "사용자가 소장한 의류 중 index 위치의 사진을 삭제하고, 남은 사진 경로 리스트를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 필요")
            }
    )
    @DeleteMapping("/photos/{index}")
    public ApiResult<List<String>> deleteClothesFromWardrobe(@Parameter(description = "삭제할 의류의 순서(0부터 시작)", example = "2")
                                                                 @PathVariable int index,
                                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        List<String> remain = wardrobeService.deleteClothes(userId, index);

        return ApiResult.ok("소장 의류 삭제 완료", remain);
    }

    @Operation(summary = "소장 의류 제공", description = "사용자가 소장한 의류 중 index 위치의 사진을 제공하고, 사진의 리소스를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "제공 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 필요")
            }
    )
    @GetMapping("/photos/{index}")
    public ResponseEntity<Resource> provideClothesFromWardrobe(@Parameter(description = "제공할 의류의 순서(0부터 시작)", example = "2")
                                                                   @PathVariable int index,
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

    @PostMapping("/photos-list")
    public ApiResult<List<String>> findWardrobeList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getId();
        List<String> remain = wardrobeService.findWardrobeAll(userId);

        return ApiResult.ok("소장 의류 목록", remain);
    }
}
