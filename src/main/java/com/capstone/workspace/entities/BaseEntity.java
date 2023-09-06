package com.capstone.workspace.entities;

import com.capstone.workspace.listeners.BaseListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@EntityListeners(BaseListener.class)
@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    protected UUID id;

    @Column(nullable = false)
    protected LocalDateTime createdAt;

    @Column(nullable = false)
    protected LocalDateTime updatedAt;

    @Column
    protected String createdBy;

    @Column
    protected String updatedBy;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    protected boolean isDeleted = false;
}