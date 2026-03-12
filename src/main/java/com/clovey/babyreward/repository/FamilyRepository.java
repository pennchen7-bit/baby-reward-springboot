package com.clovey.babyreward.repository;

import com.clovey.babyreward.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, String> {
    
    Optional<Family> findByFamilyCode(String familyCode);
    
    Optional<Family> findByName(String name);
    
    boolean existsByFamilyCode(String familyCode);
    
    boolean existsByName(String name);
}
