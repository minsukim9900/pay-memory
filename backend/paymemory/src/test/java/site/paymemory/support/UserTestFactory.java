package site.paymemory.support;

import site.paymemory.domain.user.entity.User;

public final class UserTestFactory {

    public static final String SOCIAL_ID = "kakao-123456";
    public static final String EMAIL = "test@paymemory.site";
    public static final String NICKNAME = "하파엘";
    public static final String PROFILE_IMAGE_URL = "https://image.paymemory.site/profile.png";

    public static final String NEW_NICKNAME = "새로운닉네임";
    public static final String NEW_PROFILE_IMAGE_URL = "https://image.paymemory.site/new-profile.png";

    private UserTestFactory() {
    }

    public static User createUser() {

        return User.of(SOCIAL_ID, EMAIL, NICKNAME, PROFILE_IMAGE_URL);
    }
}