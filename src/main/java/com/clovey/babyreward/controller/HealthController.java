package com.clovey.babyreward.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@CrossOrigin(origins = "*")
public class HealthController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "healthy");
        health.put("timestamp", java.time.Instant.now().toString());
        health.put("version", "1.0.0");
        health.put("environment", "production");
        
        Map<String, Object> checks = new HashMap<>();
        
        // 检查数据库连接
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            checks.put("database", "connected");
        } catch (Exception e) {
            checks.put("database", "disconnected");
            health.put("status", "unhealthy");
        }
        
        // 检查内存
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> memory = new HashMap<>();
        memory.put("used", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024 + "MB");
        memory.put("total", runtime.totalMemory() / 1024 / 1024 + "MB");
        memory.put("percentage", (runtime.totalMemory() - runtime.freeMemory()) * 100 / runtime.totalMemory());
        checks.put("memory", memory);
        
        health.put("checks", checks);
        
        int statusCode = "healthy".equals(health.get("status")) ? 200 : 503;
        return ResponseEntity.status(statusCode).body(health);
    }
}
