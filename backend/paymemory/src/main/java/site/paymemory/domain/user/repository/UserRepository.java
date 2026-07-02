package site.paymemory.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.paymemory.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialId(String socialId);
}
