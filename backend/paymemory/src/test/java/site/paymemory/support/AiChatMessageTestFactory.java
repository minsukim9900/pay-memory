package site.paymemory.support;

import site.paymemory.domain.chat.document.AiChatMessage;
import site.paymemory.domain.chat.type.MessageRole;
import site.paymemory.domain.chat.type.MessageType;

public final class AiChatMessageTestFactory {

    public static final Long AI_CHAT_ROOM_ID = 1L;
    public static final Long USER_ID = 1L;
    public static final MessageRole ROLE = MessageRole.USER;
    public static final MessageType MESSAGE_TYPE = MessageType.TEXT;
    public static final String CONTENT = "이번 달 소비 내역을 분석해줘.";

    private AiChatMessageTestFactory() {
    }

    public static AiChatMessage createAiChatMessage() {

        return AiChatMessage.of(
                AI_CHAT_ROOM_ID,
                USER_ID,
                ROLE,
                MESSAGE_TYPE,
                CONTENT
        );
    }

    public static AiChatMessage createAiChatMessage(
            Long aiChatRoomId,
            Long userId,
            MessageRole role,
            MessageType messageType,
            String content
    ) {

        return AiChatMessage.of(
                aiChatRoomId,
                userId,
                role,
                messageType,
                content
        );
    }
}