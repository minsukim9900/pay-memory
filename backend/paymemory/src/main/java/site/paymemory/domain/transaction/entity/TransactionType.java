package site.paymemory.domain.transaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {

    PAYMENT("결제"),
    CHARGE("충전"),
    TRANSFER("이체"),
    INCOME("수입");

    private final String label;
}
