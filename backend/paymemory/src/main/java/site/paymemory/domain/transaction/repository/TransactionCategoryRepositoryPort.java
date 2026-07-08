package site.paymemory.domain.transaction.repository;

import java.util.Optional;

import site.paymemory.domain.transaction.entity.TransactionCategory;

public interface TransactionCategoryRepositoryPort {

    TransactionCategory save(TransactionCategory transactionCategory);

    Optional<TransactionCategory> findByName(String name);
}