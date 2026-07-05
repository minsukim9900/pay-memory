package site.paymemory.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.paymemory.domain.user.dto.response.UserInfoResponse;
import site.paymemory.domain.user.entity.User;
import site.paymemory.domain.user.exception.UserErrorCode;
import site.paymemory.domain.user.repository.UserRepositoryPort;
import site.paymemory.global.exception.GlobalException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepositoryPort userRepositoryPort;

    public UserInfoResponse findMe(Long userId) {
        User user = userRepositoryPort.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new GlobalException(UserErrorCode.USER_NOT_FOUND));

        return UserInfoResponse.from(user);
    }
}
