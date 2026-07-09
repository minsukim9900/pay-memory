package site.paymemory.domain.transaction.normalizer;

import java.text.Normalizer;

public final class KakaoPayMerchantNameNormalizer {

    private KakaoPayMerchantNameNormalizer() {
    }

    public static String normalize(String merchantName) {

        if (merchantName == null || merchantName.isBlank()) {
            return merchantName;
        }

        return Normalizer.normalize(merchantName, Normalizer.Form.NFKC)
                .replaceAll("\\s+", "")
                .trim();
    }
}