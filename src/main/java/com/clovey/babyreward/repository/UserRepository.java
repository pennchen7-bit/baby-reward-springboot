package com.clovey.babyreward.repository;

import com.clovey.babyreward.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByWechatOpenid(String wechatOpenid);
    
    List<User> findByFamilyId(String familyId);
    
    boolean existsByUsername(String username);
    
    boolean existsByWechatOpenid(String wechatOpenid);
}
