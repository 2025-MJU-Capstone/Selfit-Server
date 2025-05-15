package selfit.selfit.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")               // API 전용 경로
                .allowedOrigins(frontendUrl)         // application.yml 에 설정한 프론트 URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")                // 모든 헤더 허용
                .allowCredentials(true)             // 쿠키, Authorization 헤더 허용
                .maxAge(3600);                      // pre-flight 캐시 1시간
    }

    // 필요하다면 아래 메서드로 static 리소스 매핑도 추가 가능
    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     registry.addResourceHandler("/static/**")
    //             .addResourceLocations("classpath:/static/");
    // }
}
