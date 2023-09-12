package com.capstone.workspace.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "document", schema = "public")
@Where(clause = "is_deleted=false")
public class Document extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private byte[] content;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private long size;

    @Column
    private UUID reference;

    @Column
    private String referenceType;
}
