package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.ProductStatus;
import com.sapo.mock_project.inventory_receipt.converts.ProductImageConverter;
import com.sapo.mock_project.inventory_receipt.converts.ProductTypeConverter;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequences")
    @GenericGenerator(
            name = "product_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.PRODUCT),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "product_sequences")
            })
    private String id;

    private String subId;

    private String name;

    @Convert(converter = ProductImageConverter.class)
    private List<ProductImage> images;

    @Convert(converter = ProductTypeConverter.class)
    private List<ProductType> types;

    private BigDecimal quantity;

    private BigDecimal sold;

    private String description;

    private String unit;

    private BigDecimal costPrice;

    private BigDecimal wholesalePrice;

    private BigDecimal retailPrice;

    private String tags;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product")
    private List<GINProduct> gins;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<GRNProduct> grnProducts;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product")
    private List<RefundInformationDetail> refundInformationDetails;

    @Override
    protected void customPrePersist() {
        if ((subId == null || subId.isEmpty()) && id != null) {
            subId = id;
        }
    }

    public void calculatorValue() {
        if (images == null) {
            images = List.of();
        }
        if (types == null) {
            types = List.of();
        }
        if (quantity == null) {
            quantity = BigDecimal.ZERO;
        }
        if (sold == null) {
            sold = BigDecimal.ZERO;
        }
        if (costPrice == null) {
            costPrice = BigDecimal.ZERO;
        }
        if (wholesalePrice == null) {
            wholesalePrice = BigDecimal.ZERO;
        }
        if (retailPrice == null) {
            retailPrice = BigDecimal.ZERO;
        }
        if (status == null) {
            status = ProductStatus.ACTIVE;
        }
    }
}
