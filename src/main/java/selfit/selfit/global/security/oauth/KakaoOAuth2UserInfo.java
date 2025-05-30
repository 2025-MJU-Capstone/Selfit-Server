package selfit.selfit.global.security.oauth;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo{

    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return properties != null ? (String) properties.get("nickname") : null;
    }


    @SuppressWarnings("unchecked")
    @Override
    public String getImageUrl() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return properties != null ? (String) properties.get("profile_image") : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
