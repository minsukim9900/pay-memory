package site.paymemory.support;

import site.paymemory.domain.chat.entity.AiChatRoom;
import site.paymemory.domain.user.entity.User;

import static site.paymemory.support.UserTestFactory.createUser;

public final class AiChatRoomTestFactory {

    public static final String TITLE = "이번 달 소비 분석";
    public static final String NEW_TITLE = "지난 달 소비 분석";

    private AiChatRoomTestFactory() {
    }

    public static AiChatRoom createAiChatRoom() {

        return AiChatRoom.of(createUser(), TITLE);
    }

    public static AiChatRoom createAiChatRoomWithUser(User user) {

        return AiChatRoom.of(user, TITLE);
    }

    public static AiChatRoom createAiChatRoomWithTitle(String title) {

        return AiChatRoom.of(createUser(), title);
    }
}