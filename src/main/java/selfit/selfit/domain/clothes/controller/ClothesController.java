package selfit.selfit.domain.clothes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "담은 옷 등록", description = "담은 옷을 등록합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 등록"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/upload")
    public ApiResult<String> registerClothes(@RequestParam("type") ClothesType type,
                                                 @RequestParam("file") MultipartFile file,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException{

        Long userId = customUserDetails.getId();
        String path = clothesService.saveClothes(userId, type, file);
        return ApiResult.ok("담은 옷 등록", path);
    }

    @Operation(summary = "담은 옷 삭제", description = "담은 옷 중 하나를 선택해 삭제합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 삭제"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @DeleteMapping("/upload/{index}")
    public ApiResult<List<String>> deleteClothes(@Parameter(description = "제공할 의류의 순서(0부터 시작)", example = "2")
                                                     @PathVariable int index,
                                           @AuthenticationPrincipal CustomUserDetails details) throws IOException{
        Long userId = details.getId();
        List<String> remain = clothesService.deleteClothes(userId, index);
        return ApiResult.ok("선택한 사진 삭제 성공", remain);
    }

    @Operation(summary = "담은 옷 제공", description = "담은 옷 중 하나를 선택해 제공합니다.",
    responses = {
            @ApiResponse(responseCode = "200", description = "성공적으로 제공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/upload/{index}")
    public ResponseEntity<Resource> provideClothes(@Parameter(description = "제공할 의류의 순서(0부터 시작)", example = "2")
                                                       @PathVariable int index,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        Long userId = userDetails.getId();
        Resource resource = clothesService.provideClothes(userId, index);

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
    @PostMapping("/upload-list")
    public ApiResult<List<String>> findClothesList(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        Long userId = customUserDetails.getId();
        List<String> remain = clothesService.findClothesAll(userId);

        return ApiResult.ok("담은 의류 목록", remain);
    }
}
