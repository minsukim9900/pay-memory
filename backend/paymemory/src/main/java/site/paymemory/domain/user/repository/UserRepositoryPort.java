package site.paymemory.domain.user.repository;

import site.paymemory.domain.user.entity.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByIdAndDeletedAtIsNull(Long id);
}
