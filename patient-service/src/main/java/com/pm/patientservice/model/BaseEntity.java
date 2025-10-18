package com.pm.patientservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreatedDate // 3. Replaced @CreationTimestamp
//    @Column(nullable = false, updatable = false)
    @Column(nullable = true, updatable = false)
    // Good practice to make it non-nullable and non-updatable
    private LocalDateTime createdAt;

    @LastModifiedDate // 4. Replaced @UpdateTimestamp
//    @Column(nullable = false) // Good practice to make it non-nullable
    @Column(nullable = true) // Good practice to make it non-nullable
    private LocalDateTime updatedAt;
}
