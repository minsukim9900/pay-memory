package site.paymemory.global.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.paymemory.domain.user.entity.User;
import site.paymemory.domain.user.repository.UserRepository;
import site.paymemory.global.security.dto.KakaoOAuth2UserInfo;
import site.paymemory.global.security.oauth.CustomOAuth2User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoOAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(oAuth2User.getAttributes());

        User user = findOrCreateUser(userInfo);

        return new CustomOAuth2User(user.getId(), oAuth2User.getAttributes());
    }

    private User findOrCreateUser(KakaoOAuth2UserInfo userInfo) {

        return userRepository.findBySocialId(userInfo.getSocialId())
                .map(user -> updateUserProfile(user, userInfo))
                .orElseGet(() -> createUser(userInfo));
    }

    private User updateUserProfile(User user, KakaoOAuth2UserInfo userInfo) {

        user.updateProfile(
                userInfo.getNickname(),
                userInfo.getProfileImageUrl()
        );

        return user;
    }

    private User createUser(KakaoOAuth2UserInfo userInfo) {

        return userRepository.save(User.of(
                userInfo.getSocialId(),
                userInfo.getEmail(),
                userInfo.getNickname(),
                userInfo.getProfileImageUrl()
        ));
    }
}