package selfit.selfit.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import selfit.selfit.domain.user.dto.UserDetailDto;
import selfit.selfit.global.dto.ApiResult;

@RestController
@RequestMapping("api/user-info")
@RequiredArgsConstructor
public class UserController {

    //swagger 사용 예시
    @Operation(summary = "유저 정보 저장", description = "주어진 유저 정보를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 저장됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/save")
    public ApiResult<String> save(@RequestBody UserDetailDto userDetailDto) {

        //저장하는 로직

        return ApiResult.ok("저장됨");
    }

}
