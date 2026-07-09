package site.paymemory.domain.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.paymemory.domain.transaction.entity.TransactionCategory;

public interface TransactionCategoryRepository extends
        JpaRepository<TransactionCategory, Long>,
        TransactionCategoryRepositoryPort {

    @Override
    Optional<TransactionCategory> findByName(String name);
}