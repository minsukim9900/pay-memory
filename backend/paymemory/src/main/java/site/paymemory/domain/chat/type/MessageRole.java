package site.paymemory.domain.chat.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageRole {

    USER("사용자"),
    ASSISTANT("AI");

    private final String label;
}