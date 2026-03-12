package com.clovey.babyreward.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {
    
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(nullable = false)
    private String familyId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familyId", insertable = false, updatable = false)
    private Family family;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String passwordHash;
    
    @Column(length = 100)
    private String wechatOpenid;
    
    @Column(nullable = false)
    private String role; // super_admin, admin, parent, baby
    
    @Column(nullable = false)
    private Boolean active;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "user")
    private List<DrawRequest> drawRequests;
    
    @OneToMany(mappedBy = "user")
    private List<DrawRecord> drawRecords;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (active == null) {
            active = true;
        }
    }
}
