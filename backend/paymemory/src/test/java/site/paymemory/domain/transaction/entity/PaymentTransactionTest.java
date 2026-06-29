package site.paymemory.domain.transaction.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.paymemory.domain.user.entity.User;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static site.paymemory.support.PaymentTransactionTestFactory.AI_IMAGE_GENERATED_AT;
import static site.paymemory.support.PaymentTransactionTestFactory.AI_IMAGE_OBJECT_KEY;
import static site.paymemory.support.PaymentTransactionTestFactory.AMOUNT;
import static site.paymemory.support.PaymentTransactionTestFactory.INCLUDED_IN_SPENDING;
import static site.paymemory.support.PaymentTransactionTestFactory.MEMO;
import static site.paymemory.support.PaymentTransactionTestFactory.MERCHANT_NAME;
import static site.paymemory.support.PaymentTransactionTestFactory.NEW_AMOUNT;
import static site.paymemory.support.PaymentTransactionTestFactory.NEW_INCLUDED_IN_SPENDING;
import static site.paymemory.support.PaymentTransactionTestFactory.NEW_MEMO;
import static site.paymemory.support.PaymentTransactionTestFactory.NEW_MERCHANT_NAME;
import static site.paymemory.support.PaymentTransactionTestFactory.NEW_TRANSACTION_AT;
import static site.paymemory.support.PaymentTransactionTestFactory.NEW_TRANSACTION_CATEGORY_NAME;
import static site.paymemory.support.PaymentTransactionTestFactory.NEW_TRANSACTION_TYPE;
import static site.paymemory.support.PaymentTransactionTestFactory.TRANSACTION_AT;
import static site.paymemory.support.PaymentTransactionTestFactory.TRANSACTION_CATEGORY_NAME;
import static site.paymemory.support.PaymentTransactionTestFactory.TRANSACTION_TYPE;
import static site.paymemory.support.PaymentTransactionTestFactory.createPaymentTransaction;
import static site.paymemory.support.PaymentTransactionTestFactory.createPaymentTransactionWithAmount;
import static site.paymemory.support.PaymentTransactionTestFactory.createPaymentTransactionWithCategory;
import static site.paymemory.support.PaymentTransactionTestFactory.createPaymentTransactionWithMerchantName;
import static site.paymemory.support.PaymentTransactionTestFactory.createPaymentTransactionWithTransactionAt;
import static site.paymemory.support.PaymentTransactionTestFactory.createPaymentTransactionWithTransactionType;
import static site.paymemory.support.PaymentTransactionTestFactory.createPaymentTransactionWithUser;
import static site.paymemory.support.TransactionCategoryTestFactory.createTransactionCategory;
import static site.paymemory.support.UserTestFactory.createUser;

class PaymentTransactionTest {

    private static final String REQUIRED_USER_MESSAGE = "사용자는 필수입니다.";
    private static final String REQUIRED_TRANSACTION_CATEGORY_MESSAGE = "거래 카테고리는 필수입니다.";
    private static final String REQUIRED_TRANSACTION_AT_MESSAGE = "거래일시는 필수입니다.";
    private static final String REQUIRED_TRANSACTION_TYPE_MESSAGE = "거래 유형은 필수입니다.";
    private static final String REQUIRED_MERCHANT_NAME_MESSAGE = "사용처는 필수입니다.";
    private static final String INVALID_AMOUNT_MESSAGE = "금액은 0보다 커야 합니다.";

    @Nested
    @DisplayName("거래내역 생성")
    class CreatePaymentTransaction {

        @Test
        @DisplayName("정상적으로 거래내역 객체를 생성한다.")
        void createPaymentTransactionSuccessfully() throws Exception {
            //given
            User user = createUser();
            TransactionCategory transactionCategory = createTransactionCategory(TRANSACTION_CATEGORY_NAME);

            //when
            PaymentTransaction paymentTransaction = createPaymentTransaction(
                    user,
                    transactionCategory,
                    TRANSACTION_AT,
                    MERCHANT_NAME,
                    AMOUNT,
                    TRANSACTION_TYPE,
                    INCLUDED_IN_SPENDING,
                    MEMO
            );

            //then
            assertThat(paymentTransaction.getUser()).isEqualTo(user);
            assertThat(paymentTransaction.getTransactionCategory()).isEqualTo(transactionCategory);
            assertThat(paymentTransaction.getTransactionAt()).isEqualTo(TRANSACTION_AT);
            assertThat(paymentTransaction.getMerchantName()).isEqualTo(MERCHANT_NAME);
            assertThat(paymentTransaction.getAmount()).isEqualTo(AMOUNT);
            assertThat(paymentTransaction.getTransactionType()).isEqualTo(TRANSACTION_TYPE);
            assertThat(paymentTransaction.isIncludedInSpending()).isEqualTo(INCLUDED_IN_SPENDING);
            assertThat(paymentTransaction.getMemo()).isEqualTo(MEMO);
            assertThat(paymentTransaction.getAiImageObjectKey()).isNull();
            assertThat(paymentTransaction.getAiImageGeneratedAt()).isNull();
        }

        @Test
        @DisplayName("user가 null이면 예외가 발생한다.")
        void throwExceptionWhenUserIsNull() throws Exception {
            //given
            User user = null;

            //when
            Throwable thrown = catchThrowable(() ->
                    createPaymentTransactionWithUser(user)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(REQUIRED_USER_MESSAGE);
        }

        @Test
        @DisplayName("transactionCategory가 null이면 예외가 발생한다.")
        void throwExceptionWhenTransactionCategoryIsNull() throws Exception {
            //given
            TransactionCategory transactionCategory = null;

            //when
            Throwable thrown = catchThrowable(() ->
                    createPaymentTransactionWithCategory(transactionCategory)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(REQUIRED_TRANSACTION_CATEGORY_MESSAGE);
        }

        @Test
        @DisplayName("transactionAt이 null이면 예외가 발생한다.")
        void throwExceptionWhenTransactionAtIsNull() throws Exception {
            //given
            Instant transactionAt = null;

            //when
            Throwable thrown = catchThrowable(() ->
                    createPaymentTransactionWithTransactionAt(transactionAt)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(REQUIRED_TRANSACTION_AT_MESSAGE);
        }

        @Test
        @DisplayName("merchantName이 null이면 예외가 발생한다.")
        void throwExceptionWhenMerchantNameIsNull() throws Exception {
            //given
            String merchantName = null;

            //when
            Throwable thrown = catchThrowable(() ->
                    createPaymentTransactionWithMerchantName(merchantName)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_MERCHANT_NAME_MESSAGE);
        }

        @Test
        @DisplayName("merchantName이 blank이면 예외가 발생한다.")
        void throwExceptionWhenMerchantNameIsBlank() throws Exception {
            //given
            String merchantName = " ";

            //when
            Throwable thrown = catchThrowable(() ->
                    createPaymentTransactionWithMerchantName(merchantName)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_MERCHANT_NAME_MESSAGE);
        }

        @Test
        @DisplayName("amount가 0이면 예외가 발생한다.")
        void throwExceptionWhenAmountIsZero() throws Exception {
            //given
            long amount = 0L;

            //when
            Throwable thrown = catchThrowable(() ->
                    createPaymentTransactionWithAmount(amount)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(INVALID_AMOUNT_MESSAGE);
        }

        @Test
        @DisplayName("amount가 음수이면 예외가 발생한다.")
        void throwExceptionWhenAmountIsNegative() throws Exception {
            //given
            long amount = -1L;

            //when
            Throwable thrown = catchThrowable(() ->
                    createPaymentTransactionWithAmount(amount)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(INVALID_AMOUNT_MESSAGE);
        }

        @Test
        @DisplayName("transactionType이 null이면 예외가 발생한다.")
        void throwExceptionWhenTransactionTypeIsNull() throws Exception {
            //given
            TransactionType transactionType = null;

            //when
            Throwable thrown = catchThrowable(() ->
                    createPaymentTransactionWithTransactionType(transactionType)
            );

            //then
            assertThat(thrown)
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(REQUIRED_TRANSACTION_TYPE_MESSAGE);
        }
    }

    @Nested
    @DisplayName("거래내역 수정")
    class UpdatePaymentTransaction {

        @Test
        @DisplayName("거래 정보를 정상적으로 수정한다.")
        void updatePaymentTransactionSuccessfully() throws Exception {
            //given
            PaymentTransaction paymentTransaction = createPaymentTransaction();
            TransactionCategory newTransactionCategory = createTransactionCategory(NEW_TRANSACTION_CATEGORY_NAME);

            //when
            paymentTransaction.updateTransaction(
                    newTransactionCategory,
                    NEW_TRANSACTION_AT,
                    NEW_MERCHANT_NAME,
                    NEW_AMOUNT,
                    NEW_TRANSACTION_TYPE,
                    NEW_INCLUDED_IN_SPENDING,
                    NEW_MEMO
            );

            //then
            assertThat(paymentTransaction.getTransactionCategory()).isEqualTo(newTransactionCategory);
            assertThat(paymentTransaction.getTransactionAt()).isEqualTo(NEW_TRANSACTION_AT);
            assertThat(paymentTransaction.getMerchantName()).isEqualTo(NEW_MERCHANT_NAME);
            assertThat(paymentTransaction.getAmount()).isEqualTo(NEW_AMOUNT);
            assertThat(paymentTransaction.getTransactionType()).isEqualTo(NEW_TRANSACTION_TYPE);
            assertThat(paymentTransaction.isIncludedInSpending()).isEqualTo(NEW_INCLUDED_IN_SPENDING);
            assertThat(paymentTransaction.getMemo()).isEqualTo(NEW_MEMO);
        }
    }

    @Nested
    @DisplayName("AI 이미지 정보 수정")
    class UpdateAiImage {

        @Test
        @DisplayName("AI 이미지 Object Key와 생성일시를 정상적으로 수정한다.")
        void updateAiImageSuccessfully() throws Exception {
            //given
            PaymentTransaction paymentTransaction = createPaymentTransaction();

            //when
            paymentTransaction.updateAiImage(AI_IMAGE_OBJECT_KEY, AI_IMAGE_GENERATED_AT);

            //then
            assertThat(paymentTransaction.getAiImageObjectKey()).isEqualTo(AI_IMAGE_OBJECT_KEY);
            assertThat(paymentTransaction.getAiImageGeneratedAt()).isEqualTo(AI_IMAGE_GENERATED_AT);
        }
    }
}