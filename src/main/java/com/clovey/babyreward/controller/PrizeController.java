package com.clovey.babyreward.controller;

import com.clovey.babyreward.dto.ApiResponse;
import com.clovey.babyreward.entity.Prize;
import com.clovey.babyreward.repository.PrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/prizes")
@CrossOrigin(origins = "*")
public class PrizeController {
    
    @Autowired
    private PrizeRepository prizeRepository;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Prize>>> getPrizes(@RequestParam(required = false) String familyId) {
        try {
            List<Prize> prizes;
            
            if (familyId != null && !familyId.isEmpty()) {
                prizes = prizeRepository.findByFamilyIdAndActiveTrueOrderByProbabilityDesc(familyId);
            } else {
                prizes = prizeRepository.findByActiveTrueOrderByProbabilityDesc();
            }
            
            return ResponseEntity.ok(ApiResponse.success(prizes));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("获取奖品失败", e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Prize>> createPrize(@RequestBody Map<String, Object> request) {
        try {
            String familyId = (String) request.get("familyId");
            String name = (String) request.get("name");
            String description = (String) request.get("description");
            Integer points = request.get("points") != null ? (Integer) request.get("points") : 0;
            Integer probability = request.get("probability") != null ? (Integer) request.get("probability") : 100;
            
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("奖品名称不能为空"));
            }
            
            Prize prize = new Prize();
            prize.setId(UUID.randomUUID().toString());
            prize.setFamilyId(familyId);
            prize.setName(name);
            prize.setDescription(description);
            prize.setPoints(points);
            prize.setProbability(probability);
            prize.setActive(true);
            
            prizeRepository.save(prize);
            
            return ResponseEntity.ok(ApiResponse.success(prize));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("创建奖品失败", e.getMessage()));
        }
    }
    
    @PutMapping
    public ResponseEntity<ApiResponse<Prize>> updatePrize(@RequestBody Map<String, Object> request) {
        try {
            String id = (String) request.get("id");
            
            if (id == null || id.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("奖品 ID 不能为空"));
            }
            
            Optional<Prize> prizeOpt = prizeRepository.findById(id);
            if (prizeOpt.isEmpty()) {
                return ResponseEntity.status(404).body(ApiResponse.error("奖品不存在"));
            }
            
            Prize prize = prizeOpt.get();
            
            if (request.get("name") != null) {
                prize.setName((String) request.get("name"));
            }
            if (request.get("description") != null) {
                prize.setDescription((String) request.get("description"));
            }
            if (request.get("points") != null) {
                prize.setPoints((Integer) request.get("points"));
            }
            if (request.get("probability") != null) {
                prize.setProbability((Integer) request.get("probability"));
            }
            if (request.get("active") != null) {
                prize.setActive((Boolean) request.get("active"));
            }
            
            prizeRepository.save(prize);
            
            return ResponseEntity.ok(ApiResponse.success(prize));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("更新奖品失败", e.getMessage()));
        }
    }
    
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deletePrize(@RequestParam String id) {
        try {
            if (id == null || id.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("缺少奖品 ID"));
            }
            
            prizeRepository.deleteById(id);
            
            return ResponseEntity.ok(ApiResponse.success(null));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("删除奖品失败", e.getMessage()));
        }
    }
}
