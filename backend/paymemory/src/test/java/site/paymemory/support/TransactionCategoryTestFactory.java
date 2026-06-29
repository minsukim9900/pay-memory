package site.paymemory.support;

import site.paymemory.domain.transaction.entity.TransactionCategory;

public final class TransactionCategoryTestFactory {

    private TransactionCategoryTestFactory() {
    }

    public static TransactionCategory createTransactionCategory(String name) {

        return TransactionCategory.fromName(name);
    }
}
