package site.paymemory.domain.transaction.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static site.paymemory.support.TransactionCategoryTestFactory.createTransactionCategory;

class TransactionCategoryTest {

    private static final String CATEGORY_NAME = "식비";
    private static final String REQUIRED_CATEGORY_NAME_MESSAGE = "거래 카테고리명은 필수입니다.";

    @Nested
    @DisplayName("거래 카테고리 생성")
    class CreateTransactionCategory {
        @Test
        @DisplayName("정상적으로 거래 카테고리 객체를 생성한다.")
        void createTransactionCategorySuccessfully() throws Exception {
            //given

            //when
            TransactionCategory transactionCategory = createTransactionCategory(CATEGORY_NAME);
            //then
            assertThat(transactionCategory.getName()).isEqualTo(CATEGORY_NAME);
        }

        @Test
        @DisplayName("name이 null이면 예외가 발생한다.")
        void throwExceptionWhenNameIsNull() throws Exception {
            //given
            String name = null;
            //when
            Throwable thrown = catchThrowable(() ->
                    createTransactionCategory(name)
            );
            //then
            assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_CATEGORY_NAME_MESSAGE);
        }

        @Test
        @DisplayName("name이 blank이면 예외가 발생한다.")
        void throwExceptionWhenNameIsBlank() throws Exception {
            //given
            String name = " ";
            //when
            Throwable thrown = catchThrowable(() ->
                    createTransactionCategory(name)
            );
            //then
            assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(REQUIRED_CATEGORY_NAME_MESSAGE);
        }
    }
}