package site.paymemory.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static site.paymemory.support.UserTestFactory.EMAIL;
import static site.paymemory.support.UserTestFactory.NICKNAME;
import static site.paymemory.support.UserTestFactory.PROFILE_IMAGE_URL;
import static site.paymemory.support.UserTestFactory.createUser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import site.paymemory.domain.user.dto.response.UserInfoResponse;
import site.paymemory.domain.user.entity.User;
import site.paymemory.domain.user.exception.UserErrorCode;
import site.paymemory.global.exception.GlobalException;
import site.paymemory.support.FakeUserRepositoryPort;

class UserServiceTest {

    private static final Long USER_ID = 1L;

    private final FakeUserRepositoryPort userRepositoryPort = new FakeUserRepositoryPort();
    private final UserService userService = new UserService(userRepositoryPort);

    @Nested
    @DisplayName("내 정보 조회")
    class FindMe {

        @Test
        @DisplayName("사용자가 존재하면 내 정보를 반환한다")
        void givenExistingUserId_whenFindMe_thenReturnsUserInfo() {
            // given
            User user = createUser();

            userRepositoryPort.save(USER_ID, user);

            // when
            UserInfoResponse response = userService.findMe(USER_ID);

            // then
            assertThat(response.userId()).isEqualTo(USER_ID);
            assertThat(response.email()).isEqualTo(EMAIL);
            assertThat(response.nickname()).isEqualTo(NICKNAME);
            assertThat(response.profileImageUrl()).isEqualTo(PROFILE_IMAGE_URL);
        }

        @Test
        @DisplayName("사용자가 존재하지 않으면 예외가 발생한다")
        void givenNotExistingUserId_whenFindMe_thenThrowsException() {
            // when
            Throwable thrown = catchThrowable(() -> userService.findMe(USER_ID));

            // then
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class);

            GlobalException exception = (GlobalException) thrown;

            assertThat(exception.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);
        }

        @Test
        @DisplayName("탈퇴한 사용자는 조회되지 않고 예외가 발생한다")
        void givenDeletedUserId_whenFindMe_thenThrowsException() {
            // given
            User user = createUser();

            userRepositoryPort.save(USER_ID, user);
            userRepositoryPort.delete(USER_ID);

            // when
            Throwable thrown = catchThrowable(() -> userService.findMe(USER_ID));

            // then
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class);

            GlobalException exception = (GlobalException) thrown;

            assertThat(exception.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("회원 탈퇴")
    class DeleteMe {

        @Test
        @DisplayName("사용자가 존재하면 회원 탈퇴 처리한다")
        void givenExistingUserId_whenDeleteMe_thenDeletesUser() {
            // given
            User user = createUser();

            userRepositoryPort.save(USER_ID, user);

            // when
            userService.deleteMe(USER_ID);

            // then
            assertThat(user.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("사용자가 존재하지 않으면 예외가 발생한다")
        void givenNotExistingUserId_whenDeleteMe_thenThrowsException() {
            // when
            Throwable thrown = catchThrowable(() -> userService.deleteMe(USER_ID));

            // then
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class);

            GlobalException exception = (GlobalException) thrown;

            assertThat(exception.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);
        }

        @Test
        @DisplayName("이미 탈퇴한 사용자는 예외가 발생한다")
        void givenDeletedUserId_whenDeleteMe_thenThrowsException() {
            // given
            User user = createUser();

            userRepositoryPort.save(USER_ID, user);
            userRepositoryPort.delete(USER_ID);

            // when
            Throwable thrown = catchThrowable(() -> userService.deleteMe(USER_ID));

            // then
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class);

            GlobalException exception = (GlobalException) thrown;

            assertThat(exception.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);
        }
    }
}