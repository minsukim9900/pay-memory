package site.paymemory.domain.transaction.normalizer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KakaoPayMerchantNameNormalizerTest {

    @Nested
    @DisplayName("카카오페이 사용처 정규화")
    class Normalize {

        @Test
        @DisplayName("전각 영문과 숫자를 일반 문자로 변환한다")
        void givenFullWidthCharacters_whenNormalize_thenReturnsHalfWidthCharacters() {
            // given
            String merchantName = "ＧＳ２５관악원";

            // when
            String result = KakaoPayMerchantNameNormalizer.normalize(merchantName);

            // then
            assertThat(result).isEqualTo("GS25관악원");
        }

        @Test
        @DisplayName("일반 공백을 제거한다")
        void givenMerchantNameWithNormalSpace_whenNormalize_thenRemovesSpace() {
            // given
            String merchantName = "투썸플레이스 서울대";

            // when
            String result = KakaoPayMerchantNameNormalizer.normalize(merchantName);

            // then
            assertThat(result).isEqualTo("투썸플레이스서울대");
        }

        @Test
        @DisplayName("특수 공백을 제거한다")
        void givenMerchantNameWithSpecialSpace_whenNormalize_thenRemovesSpace() {
            // given
            String merchantName = "씨유　서울대파";

            // when
            String result = KakaoPayMerchantNameNormalizer.normalize(merchantName);

            // then
            assertThat(result).isEqualTo("씨유서울대파");
        }

        @Test
        @DisplayName("앞뒤 공백을 제거한다")
        void givenMerchantNameWithLeadingAndTrailingSpace_whenNormalize_thenReturnsTrimmedValue() {
            // given
            String merchantName = "  대빵인형뽑기　 ";

            // when
            String result = KakaoPayMerchantNameNormalizer.normalize(merchantName);

            // then
            assertThat(result).isEqualTo("대빵인형뽑기");
        }

        @Test
        @DisplayName("null이면 null을 반환한다")
        void givenNullMerchantName_whenNormalize_thenReturnsNull() {
            // given
            String merchantName = null;

            // when
            String result = KakaoPayMerchantNameNormalizer.normalize(merchantName);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("blank이면 기존 값을 반환한다")
        void givenBlankMerchantName_whenNormalize_thenReturnsOriginalValue() {
            // given
            String merchantName = " ";

            // when
            String result = KakaoPayMerchantNameNormalizer.normalize(merchantName);

            // then
            assertThat(result).isEqualTo(" ");
        }
    }
}