package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionStatus;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_sequences")
    @GenericGenerator(
            name = "transaction_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.TRANSACTION),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "transaction_sequences")
            })
    private String id;

    private String subId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionMethod paymentMethod;

    private String tags;

    private String note;

    private String referenceCode;

    private String referenceId;

    private String recipientGroup;

    private String recipientId;

    private String recipientName;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "transaction_category_id")
    private TransactionCategory category;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    private User userCreated;

    @Override
    protected void customPrePersist() {
        if ((subId == null || subId.isEmpty()) && id != null) {
            subId = id;
        }
    }
}
