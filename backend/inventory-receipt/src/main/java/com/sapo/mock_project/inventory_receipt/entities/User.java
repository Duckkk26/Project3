package com.sapo.mock_project.inventory_receipt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(generator = "user_sequences")
    @GenericGenerator(
            name = "user_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.USER),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "user_sequences")
            })
    private String id;

    private String subId;

    private String fullName;

    private String phone;

    private String email;

    @JsonIgnore
    private String password;

    private short gender;

    private String avatar;

    private boolean isActive;

    private boolean isDeleted;

    private Date lastChangePass;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @OneToMany(mappedBy = "userCreated")
    private List<GIN> createdGIN;

    @OneToMany(mappedBy = "userBalanced")
    private List<GIN> balancedGIN;

    @OneToMany(mappedBy = "userInspection")
    private List<GIN> inspectionGIN;

    @OneToMany(mappedBy = "userCreated")
    private List<Order> createdOrders;

    @OneToMany(mappedBy = "userCompleted")
    private List<Order> completedOrders;

    @OneToMany(mappedBy = "userCancelled")
    private List<Order> cancelledOrders;

    @OneToMany(mappedBy = "userCreated")
    private List<RefundInformation> createdRefundInformations;

    @OneToMany(mappedBy = "userCreated")
    private List<GRN> createdGRNs;

    @OneToMany(mappedBy = "userCompleted")
    private List<GRN> completedGRNs;

    @OneToMany(mappedBy = "userCancelled")
    private List<GRN> cancelledGRNs;

    @OneToMany(mappedBy = "userImported")
    private List<GRN> importedGRNs;

    @OneToMany(mappedBy = "userCreated")
    private List<Transaction> createdTransactions;

    @OneToMany(mappedBy = "userCreated")
    private List<DebtSupplier> createdDebtSuppliers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRole()));

        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    protected void customPrePersist() {
        if ((subId == null || subId.isEmpty()) && id != null) {
            subId = id;
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        lastChangePass = new Date(System.currentTimeMillis());
        isDeleted = false;
    }
}
