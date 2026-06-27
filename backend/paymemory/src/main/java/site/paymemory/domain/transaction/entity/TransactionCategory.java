package site.paymemory.domain.transaction.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@Entity
@Table(name = "transaction_category")
@NoArgsConstructor(access = PROTECTED)
public class TransactionCategory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "transaction_category_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Builder(access = PRIVATE)
    private TransactionCategory(String name) {
        this.name = name;
    }

    public static TransactionCategory fromName(String name) {

        return TransactionCategory
                .builder()
                .name(name)
                .build();
    }
}
