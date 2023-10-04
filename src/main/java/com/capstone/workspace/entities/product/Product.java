package com.capstone.workspace.entities.product;

import com.capstone.workspace.entities.partner.IPartnerEntity;
import com.capstone.workspace.entities.shared.BaseEntity;
import com.capstone.workspace.entities.store.IStoreEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.List;

@Data
@Entity
@Table(name = "product", schema = "public")
@Where(clause = "is_deleted=false")
public class Product extends BaseEntity implements IPartnerEntity, IStoreEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long price;

    @Column
    private String coverImage;

    @Column
    private String description;

    @Convert(attributeName = "categories")
    @Column
    private List<String> categories;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isSoldOut = false;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductOptionSection> productOptionSections;

    @Column
    private String partnerId;

    @Column
    private String storeId;
}