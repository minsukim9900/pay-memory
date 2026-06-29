package site.paymemory.domain.chat.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.paymemory.domain.user.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static site.paymemory.support.AiChatRoomTestFactory.NEW_TITLE;
import static site.paymemory.support.AiChatRoomTestFactory.TITLE;
import static site.paymemory.support.AiChatRoomTestFactory.createAiChatRoom;
import static site.paymemory.support.AiChatRoomTestFactory.createAiChatRoomWithTitle;
import static site.paymemory.support.AiChatRoomTestFactory.createAiChatRoomWithUser;
import static site.paymemory.support.UserTestFactory.createUser;

class AiChatRoomTest {

    private static final String REQUIRED_USER_MESSAGE = "사용자는 필수입니다.";
    private static final String REQUIRED_TITLE_MESSAGE = "AI 채팅방 제목은 필수입니다.";

    @Nested
    @DisplayName("AI 채팅방 생성")
    class CreateAiChatRoom {

        @Test
        @DisplayName("정상적으로 AI 채팅방 객체를 생성한다.")
        void createAiChatRoomSuccessfully() throws Exception {
            //given
            User user = createUser();

            //when
            AiChatRoom aiChatRoom = AiChatRoom.of(user, TITLE);

            //then
            assertThat(aiChatRoom.getUser()).isEqualTo(user);
            assertThat(aiChatRoom.getTitle()).isEqualTo(TITLE);
            assertThat(aiChatRoom.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("user가 null이면 예외가 발생한다.")
        void throwExceptionWhenUserIsNull() throws Exception {
            //given
            User user = null;

            //when
            Throwable thrown = catchThrowable(() ->
                    createAiChatRoomWithUser(user)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(REQUIRED_USER_MESSAGE);
        }

        @Test
        @DisplayName("title이 null이면 예외가 발생한다.")
        void throwExceptionWhenTitleIsNull() throws Exception {
            //given
            String title = null;

            //when
            Throwable thrown = catchThrowable(() ->
                    createAiChatRoomWithTitle(title)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_TITLE_MESSAGE);
        }

        @Test
        @DisplayName("title이 blank이면 예외가 발생한다.")
        void throwExceptionWhenTitleIsBlank() throws Exception {
            //given
            String title = " ";

            //when
            Throwable thrown = catchThrowable(() ->
                    createAiChatRoomWithTitle(title)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_TITLE_MESSAGE);
        }
    }

    @Nested
    @DisplayName("AI 채팅방 제목 수정")
    class UpdateTitle {

        @Test
        @DisplayName("AI 채팅방 제목을 정상적으로 수정한다.")
        void updateTitleSuccessfully() throws Exception {
            //given
            AiChatRoom aiChatRoom = createAiChatRoom();

            //when
            aiChatRoom.updateTitle(NEW_TITLE);

            //then
            assertThat(aiChatRoom.getTitle()).isEqualTo(NEW_TITLE);
        }

        @Test
        @DisplayName("수정할 title이 null이면 예외가 발생한다.")
        void throwExceptionWhenUpdateTitleIsNull() throws Exception {
            //given
            AiChatRoom aiChatRoom = createAiChatRoom();
            String title = null;

            //when
            Throwable thrown = catchThrowable(() ->
                    aiChatRoom.updateTitle(title)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_TITLE_MESSAGE);
        }

        @Test
        @DisplayName("수정할 title이 blank이면 예외가 발생한다.")
        void throwExceptionWhenUpdateTitleIsBlank() throws Exception {
            //given
            AiChatRoom aiChatRoom = createAiChatRoom();
            String title = " ";

            //when
            Throwable thrown = catchThrowable(() ->
                    aiChatRoom.updateTitle(title)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_TITLE_MESSAGE);
        }
    }

    @Nested
    @DisplayName("AI 채팅방 삭제")
    class DeleteAiChatRoom {

        @Test
        @DisplayName("AI 채팅방을 삭제하면 deletedAt이 설정된다.")
        void deleteAiChatRoomSuccessfully() throws Exception {
            //given
            AiChatRoom aiChatRoom = createAiChatRoom();

            //when
            aiChatRoom.delete();

            //then
            assertThat(aiChatRoom.getDeletedAt()).isNotNull();
        }
    }
}