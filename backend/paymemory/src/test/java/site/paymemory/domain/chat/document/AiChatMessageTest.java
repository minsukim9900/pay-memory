package site.paymemory.domain.chat.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static site.paymemory.support.AiChatMessageTestFactory.AI_CHAT_ROOM_ID;
import static site.paymemory.support.AiChatMessageTestFactory.CONTENT;
import static site.paymemory.support.AiChatMessageTestFactory.MESSAGE_TYPE;
import static site.paymemory.support.AiChatMessageTestFactory.ROLE;
import static site.paymemory.support.AiChatMessageTestFactory.USER_ID;
import static site.paymemory.support.AiChatMessageTestFactory.createAiChatMessage;

class AiChatMessageTest {

    @Nested
    @DisplayName("AI 채팅 메시지 생성")
    class CreateAiChatMessage {

        @Test
        @DisplayName("정상적으로 AI 채팅 메시지 객체를 생성한다.")
        void createAiChatMessageSuccessfully() throws Exception {
            //given

            //when
            AiChatMessage aiChatMessage = createAiChatMessage();

            //then
            assertThat(aiChatMessage.getAiChatRoomId()).isEqualTo(AI_CHAT_ROOM_ID);
            assertThat(aiChatMessage.getUserId()).isEqualTo(USER_ID);
            assertThat(aiChatMessage.getRole()).isEqualTo(ROLE);
            assertThat(aiChatMessage.getMessageType()).isEqualTo(MESSAGE_TYPE);
            assertThat(aiChatMessage.getContent()).isEqualTo(CONTENT);
            assertThat(aiChatMessage.getCreatedAt()).isNotNull();
            assertThat(aiChatMessage.getDeletedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("AI 채팅 메시지 삭제")
    class DeleteAiChatMessage {

        @Test
        @DisplayName("AI 채팅 메시지를 삭제하면 deletedAt이 설정된다.")
        void deleteAiChatMessageSuccessfully() throws Exception {
            //given
            AiChatMessage aiChatMessage = createAiChatMessage();

            //when
            aiChatMessage.delete();

            //then
            assertThat(aiChatMessage.getDeletedAt()).isNotNull();
        }
    }
}