package com.clovey.babyreward.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Prize")
public class Prize {
    
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(nullable = false)
    private String familyId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familyId", insertable = false, updatable = false)
    private Family family;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Integer points;
    
    @Column(nullable = false)
    private Integer probability;
    
    @Column(nullable = false)
    private Boolean active;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "prize")
    private List<DrawRecord> drawRecords;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (active == null) {
            active = true;
        }
        if (points == null) {
            points = 0;
        }
        if (probability == null) {
            probability = 100;
        }
    }
}
