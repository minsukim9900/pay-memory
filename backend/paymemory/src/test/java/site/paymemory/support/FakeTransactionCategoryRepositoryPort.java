package site.paymemory.support;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import site.paymemory.domain.transaction.entity.TransactionCategory;
import site.paymemory.domain.transaction.repository.TransactionCategoryRepositoryPort;

public class FakeTransactionCategoryRepositoryPort implements TransactionCategoryRepositoryPort {

    private final Map<String, TransactionCategory> transactionCategories = new HashMap<>();
    private long sequence = 1L;

    @Override
    public TransactionCategory save(TransactionCategory transactionCategory) {

        setField(transactionCategory, "id", sequence++);
        transactionCategories.put(transactionCategory.getName(), transactionCategory);

        return transactionCategory;
    }

    @Override
    public Optional<TransactionCategory> findByName(String name) {

        return Optional.ofNullable(transactionCategories.get(name));
    }

    private void setField(
            TransactionCategory transactionCategory,
            String fieldName,
            Object value
    ) {

        try {
            Field field = TransactionCategory.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(transactionCategory, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("TransactionCategory 테스트 필드 설정에 실패했습니다.", e);
        }
    }
}