package site.paymemory.domain.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static site.paymemory.support.UserTestFactory.EMAIL;
import static site.paymemory.support.UserTestFactory.NEW_NICKNAME;
import static site.paymemory.support.UserTestFactory.NEW_PROFILE_IMAGE_URL;
import static site.paymemory.support.UserTestFactory.NICKNAME;
import static site.paymemory.support.UserTestFactory.PROFILE_IMAGE_URL;
import static site.paymemory.support.UserTestFactory.SOCIAL_ID;
import static site.paymemory.support.UserTestFactory.createUser;

class UserTest {

    private static final String REQUIRED_SOCIAL_ID_MESSAGE = "소셜 로그인 식별자는 필수입니다.";
    private static final String REQUIRED_EMAIL_MESSAGE = "이메일은 필수입니다.";
    private static final String REQUIRED_NICKNAME_MESSAGE = "닉네임은 필수입니다.";
    private static final String REQUIRED_PROFILE_IMAGE_URL_MESSAGE = "프로필 이미지 URL은 필수입니다.";

    @Nested
    @DisplayName("사용자 생성")
    class CreateUser {

        @Test
        @DisplayName("정상적으로 사용자 객체를 생성한다")
        void createUserSuccessfully() {
            // given

            // when
            User user = User.of(SOCIAL_ID, EMAIL, NICKNAME, PROFILE_IMAGE_URL);

            // then
            assertThat(user.getSocialId()).isEqualTo(SOCIAL_ID);
            assertThat(user.getEmail()).isEqualTo(EMAIL);
            assertThat(user.getNickname()).isEqualTo(NICKNAME);
            assertThat(user.getProfileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
            assertThat(user.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("socialId가 null이면 예외가 발생한다")
        void throwExceptionWhenSocialIdIsNull() {
            // given
            String socialId = null;

            // when
            Throwable thrown = catchThrowable(() ->
                    User.of(socialId, EMAIL, NICKNAME, PROFILE_IMAGE_URL)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_SOCIAL_ID_MESSAGE);
        }

        @Test
        @DisplayName("socialId가 blank이면 예외가 발생한다")
        void throwExceptionWhenSocialIdIsBlank() {
            // given
            String socialId = " ";

            // when
            Throwable thrown = catchThrowable(() ->
                    User.of(socialId, EMAIL, NICKNAME, PROFILE_IMAGE_URL)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_SOCIAL_ID_MESSAGE);
        }

        @Test
        @DisplayName("email이 null이면 예외가 발생한다")
        void throwExceptionWhenEmailIsNull() {
            // given
            String email = null;

            // when
            Throwable thrown = catchThrowable(() ->
                    User.of(SOCIAL_ID, email, NICKNAME, PROFILE_IMAGE_URL)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_EMAIL_MESSAGE);
        }

        @Test
        @DisplayName("email이 blank이면 예외가 발생한다")
        void throwExceptionWhenEmailIsBlank() {
            // given
            String email = " ";

            // when
            Throwable thrown = catchThrowable(() ->
                    User.of(SOCIAL_ID, email, NICKNAME, PROFILE_IMAGE_URL)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_EMAIL_MESSAGE);
        }

        @Test
        @DisplayName("nickname이 null이면 예외가 발생한다")
        void throwExceptionWhenNicknameIsNull() {
            // given
            String nickname = null;

            // when
            Throwable thrown = catchThrowable(() ->
                    User.of(SOCIAL_ID, EMAIL, nickname, PROFILE_IMAGE_URL)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_NICKNAME_MESSAGE);
        }

        @Test
        @DisplayName("nickname이 blank이면 예외가 발생한다")
        void throwExceptionWhenNicknameIsBlank() {
            // given
            String nickname = " ";

            // when
            Throwable thrown = catchThrowable(() ->
                    User.of(SOCIAL_ID, EMAIL, nickname, PROFILE_IMAGE_URL)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_NICKNAME_MESSAGE);
        }

        @Test
        @DisplayName("profileImageUrl이 null이면 예외가 발생한다")
        void throwExceptionWhenProfileImageUrlIsNull() {
            // given
            String profileImageUrl = null;

            // when
            Throwable thrown = catchThrowable(() ->
                    User.of(SOCIAL_ID, EMAIL, NICKNAME, profileImageUrl)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_PROFILE_IMAGE_URL_MESSAGE);
        }

        @Test
        @DisplayName("profileImageUrl이 blank이면 예외가 발생한다")
        void throwExceptionWhenProfileImageUrlIsBlank() {
            // given
            String profileImageUrl = " ";

            // when
            Throwable thrown = catchThrowable(() ->
                    User.of(SOCIAL_ID, EMAIL, NICKNAME, profileImageUrl)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_PROFILE_IMAGE_URL_MESSAGE);
        }
    }

    @Nested
    @DisplayName("프로필 수정")
    class UpdateProfile {

        @Test
        @DisplayName("닉네임과 프로필 이미지 URL을 정상적으로 수정한다")
        void updateProfileSuccessfully() {
            // given
            User user = createUser();

            // when
            user.updateProfile(NEW_NICKNAME, NEW_PROFILE_IMAGE_URL);

            // then
            assertThat(user.getNickname()).isEqualTo(NEW_NICKNAME);
            assertThat(user.getProfileImageUrl()).isEqualTo(NEW_PROFILE_IMAGE_URL);
        }

        @Test
        @DisplayName("수정할 nickname이 null이면 예외가 발생한다")
        void throwExceptionWhenUpdateNicknameIsNull() {
            // given
            User user = createUser();
            String nickname = null;

            // when
            Throwable thrown = catchThrowable(() ->
                    user.updateProfile(nickname, NEW_PROFILE_IMAGE_URL)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_NICKNAME_MESSAGE);
        }

        @Test
        @DisplayName("수정할 nickname이 blank이면 예외가 발생한다")
        void throwExceptionWhenUpdateNicknameIsBlank() {
            // given
            User user = createUser();
            String nickname = " ";

            // when
            Throwable thrown = catchThrowable(() ->
                    user.updateProfile(nickname, NEW_PROFILE_IMAGE_URL)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_NICKNAME_MESSAGE);
        }

        @Test
        @DisplayName("수정할 profileImageUrl이 null이면 예외가 발생한다")
        void throwExceptionWhenUpdateProfileImageUrlIsNull() {
            // given
            User user = createUser();
            String profileImageUrl = null;

            // when
            Throwable thrown = catchThrowable(() ->
                    user.updateProfile(NEW_NICKNAME, profileImageUrl)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_PROFILE_IMAGE_URL_MESSAGE);
        }

        @Test
        @DisplayName("수정할 profileImageUrl이 blank이면 예외가 발생한다")
        void throwExceptionWhenUpdateProfileImageUrlIsBlank() {
            // given
            User user = createUser();
            String profileImageUrl = " ";

            // when
            Throwable thrown = catchThrowable(() ->
                    user.updateProfile(NEW_NICKNAME, profileImageUrl)
            );

            // then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_PROFILE_IMAGE_URL_MESSAGE);
        }
    }

    @Nested
    @DisplayName("사용자 삭제")
    class DeleteUser {

        @Test
        @DisplayName("사용자를 삭제하면 deletedAt이 설정된다")
        void deleteUserSuccessfully() {
            // given
            User user = createUser();

            // when
            user.delete();

            // then
            assertThat(user.getDeletedAt()).isNotNull();
        }
    }
}