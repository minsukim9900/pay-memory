package site.paymemory.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.paymemory.domain.user.entity.User;
import site.paymemory.global.entity.BaseTimeEntity;

import java.time.Instant;
import java.util.Objects;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@Entity
@Table(name = "ai_chat_room")
@NoArgsConstructor(access = PROTECTED)
public class AiChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ai_chat_room_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Builder(access = PRIVATE)
    private AiChatRoom(User user, String title) {
        validateRequiredFields(user, title);

        this.user = user;
        this.title = title;
    }

    public static AiChatRoom of(User user, String title) {

        return AiChatRoom
                .builder()
                .user(user)
                .title(title)
                .build();
    }

    public void updateTitle(String title) {
        validateTitle(title);

        this.title = title;
    }

    public void delete() {
        this.deletedAt = Instant.now();
    }

    private void validateRequiredFields(User user, String title) {
        Objects.requireNonNull(user, "사용자는 필수입니다.");
        validateTitle(title);
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("AI 채팅방 제목은 필수입니다.");
        }
    }
}