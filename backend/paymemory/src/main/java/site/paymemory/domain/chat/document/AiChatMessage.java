package site.paymemory.domain.chat.document;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import site.paymemory.domain.chat.type.MessageRole;
import site.paymemory.domain.chat.type.MessageType;

@Getter
@Document(collection = "ai_chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiChatMessage {

    @Id
    private String id;

    @Field("aiChatRoomId")
    private Long aiChatRoomId;

    @Field("userId")
    private Long userId;

    @Field("role")
    private MessageRole role;

    @Field("messageType")
    private MessageType messageType;

    @Field("content")
    private String content;

    @Field("createdAt")
    private Instant createdAt;

    @Field("deletedAt")
    private Instant deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private AiChatMessage(
            Long aiChatRoomId,
            Long userId,
            MessageRole role,
            MessageType messageType,
            String content
    ) {
        this.aiChatRoomId = aiChatRoomId;
        this.userId = userId;
        this.role = role;
        this.messageType = messageType;
        this.content = content;
        this.createdAt = Instant.now();
    }

    public static AiChatMessage of(
            Long aiChatRoomId,
            Long userId,
            MessageRole role,
            MessageType messageType,
            String content
    ) {

        return AiChatMessage
                .builder()
                .aiChatRoomId(aiChatRoomId)
                .userId(userId)
                .role(role)
                .messageType(messageType)
                .content(content)
                .build();
    }

    public void delete() {
        this.deletedAt = Instant.now();
    }
}