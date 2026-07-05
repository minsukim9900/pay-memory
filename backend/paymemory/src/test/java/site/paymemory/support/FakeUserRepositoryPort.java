package site.paymemory.support;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import site.paymemory.domain.user.entity.User;
import site.paymemory.domain.user.repository.UserRepositoryPort;

public class FakeUserRepositoryPort implements UserRepositoryPort {

    private final Map<Long, User> users = new HashMap<>();

    public void save(Long userId, User user) {

        setField(user, "id", userId);
        users.put(userId, user);
    }

    public void delete(Long userId) {

        User user = users.get(userId);

        if (user != null) {
            setField(user, "deletedAt", Instant.now());
        }
    }

    @Override
    public Optional<User> findByIdAndDeletedAtIsNull(Long id) {

        return Optional.ofNullable(users.get(id))
                .filter(user -> user.getDeletedAt() == null);
    }

    private void setField(User user, String fieldName, Object value) {

        try {
            Field field = User.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(user, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("User 테스트 필드 설정에 실패했습니다.", e);
        }
    }
}