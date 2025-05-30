package selfit.selfit.global.security.oauth;

import java.util.Map;

/**
 * 구글이나 네이버 추가 시 사용
 */
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equalsIgnoreCase(registrationId)) {
            return new KakaoOAuth2UserInfo(attributes);
        }
        throw new IllegalArgumentException("Unsupported provider: " + registrationId);
    }
}
