package selfit.selfit.global.security.oauth;

import java.util.Map;

public interface OAuth2UserInfo {
    String getEmail();

    String getName();

    /**
     * 구글이나 네이버 추가 시 사용
     */
    String getProvider();
    String getProviderId();
    String getImageUrl();
    Map<String, Object> getAttributes();
}
