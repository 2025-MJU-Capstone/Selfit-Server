package selfit.selfit.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// controller가 매핑하지 않은 모든 URL을 index.html로 포워딩
// SPA
@Configuration
public class SpaRedirectConfig implements WebMvcConfigurer {
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry){
//        // 1) “/{spring:\w+}” 패턴에 매핑된 모든 GET 요청을
//        //    내부적으로 “forward:/index.html” (리소스를 다시 내부 호출) 하도록 설정
//        registry.addViewController("/{spring:\\w+}")
//                .setViewName("forward:/index.html");
//
//        // 2) “/**/{spring:\w+}” — 하위 경로 아무 깊이에서 “단어(alphanumeric) 하나”만 있는 요청도 index.html 로
//        registry.addViewController("/**/{spring:\\w+}")
//                .setViewName("forward:/index.html");
//
//        // 3) “/{spring:\w+}/**{spring:?!(\.js|\.css)$}” —
//        //    URL 중간에 단어가 하나 있고, 그 뒤에 “.js”나 “.css” 확장자가 붙지 않은 하위 경로는 모두 index.html 로
//        registry.addViewController("/{spring:\\w+}/**{spring:?!(\\.js|\\.css)$}")
//                .setViewName("forward:/index.html");
//
//    }
}
