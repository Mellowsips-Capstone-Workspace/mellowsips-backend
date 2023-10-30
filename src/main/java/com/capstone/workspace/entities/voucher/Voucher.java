package com.capstone.workspace.entities.voucher;

import com.capstone.workspace.entities.partner.IPartnerEntity;
import com.capstone.workspace.entities.shared.BaseEntity;
import com.capstone.workspace.entities.store.IStoreEntity;
import com.capstone.workspace.enums.voucher.VoucherDiscountType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.Instant;

@Data
@Entity
@Table(name = "voucher", schema = "public")
@Where(clause = "is_deleted=false")
public class Voucher extends BaseEntity implements IPartnerEntity, IStoreEntity {
    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VoucherDiscountType discountType;

    @Column
    private Instant startDate;

    @Column
    private Instant endDate;

    @Column(nullable = false)
    private int maxUsesPerUser;

    @Column
    private Long maxDiscountAmount;

    @Column(nullable = false)
    private Long minOrderAmount;

    @Column(nullable = false, unique = true, length = 9)
    private String code;

    @Column
    private String partnerId;

    @Column
    private String storeId;
}
