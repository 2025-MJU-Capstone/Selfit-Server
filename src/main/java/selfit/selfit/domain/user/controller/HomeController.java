package selfit.selfit.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import selfit.selfit.global.dto.ApiResult;
import selfit.selfit.global.exception.ErrorCode;


@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    // 홈페이지
//    @GetMapping("")
//    public ApiResult<String> home(Authentication auth) {
////        if (auth != null && auth.isAuthenticated()){
////            String accountId = auth.getName();
////            return ApiResult.ok(accountId + "로그인 상태입니다.");
////        }
////        else{
////            return ApiResult.ok("로그인 하세요.");
////        }
//        return;
//    }

}
