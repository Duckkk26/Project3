package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierStatus;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplier extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_sequences")
    @GenericGenerator(
            name = "supplier_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.SUPPLIER),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "supplier_sequences")
            })
    private String id;

    private String subId;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String tags;

    private String note;

    @Enumerated(EnumType.STRING)
    private SupplierStatus status;

    private BigDecimal currentDebt;

    private BigDecimal totalRefund;

    @ManyToOne
    @JoinColumn(name = "supplier_group_id")
    private SupplierGroup group;

    @OneToMany(mappedBy = "supplier")
    private List<GRN> grns;

    @OneToMany(mappedBy = "supplier")
    private List<Order> orders;

    @OneToMany(mappedBy = "supplier")
    private List<DebtSupplier> debtSuppliers;

    @Override
    protected void customPrePersist() {
        if ((subId == null || subId.isEmpty()) && id != null) {
            subId = id;
        }
    }
}
