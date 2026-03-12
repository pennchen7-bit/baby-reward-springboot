package com.clovey.babyreward.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "DrawRecord")
public class DrawRecord {
    
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(nullable = false)
    private String familyId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familyId", insertable = false, updatable = false)
    private Family family;
    
    @Column(nullable = false)
    private String userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    
    @Column(nullable = false)
    private String prizeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prizeId", insertable = false, updatable = false)
    private Prize prize;
    
    @Column(nullable = false)
    private Integer points;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
