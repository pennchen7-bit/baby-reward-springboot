package com.clovey.babyreward.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Family")
public class Family {
    
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true, length = 10)
    private String familyCode;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "family")
    private List<User> users;
    
    @OneToMany(mappedBy = "family")
    private List<Prize> prizes;
    
    @OneToMany(mappedBy = "family")
    private List<DrawRequest> drawRequests;
    
    @OneToMany(mappedBy = "family")
    private List<DrawRecord> drawRecords;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
