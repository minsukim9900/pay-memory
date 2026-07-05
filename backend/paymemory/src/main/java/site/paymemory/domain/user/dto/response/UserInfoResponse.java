package site.paymemory.domain.user.dto.response;

public record UserInfoResponse(
        Long userId,
        String email,
        String nickname,
        String profileImageUrl
) {
}