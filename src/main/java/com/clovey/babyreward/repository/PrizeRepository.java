package com.clovey.babyreward.repository;

import com.clovey.babyreward.entity.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, String> {
    
    List<Prize> findByFamilyIdOrderByProbabilityDesc(String familyId);
    
    List<Prize> findByFamilyIdAndActiveTrueOrderByProbabilityDesc(String familyId);
    
    List<Prize> findByActiveTrueOrderByProbabilityDesc();
}
