package selfit.selfit.global.security.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.global.security.springsecurity.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        // attributes에서 필요한 정보 추출
        OAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(attributes);

        String email = userInfo.getEmail();
        String nickName = userInfo.getName();

        // DB에 사용자 정보 확인 및 저장/업데이트
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                            User u =User.builder()
                                    .accountId(email)
                                    .build();
                            return u;
                        }
                );

        user.setNickname(nickName);
        user.setUpdate_date(LocalDateTime.now());
        userRepository.save(user);

        // CustomUserDetails: OAuth2User + UserDetails 구현체
        return new CustomUserDetails(user, oauth2User.getAttributes());
    }
}
