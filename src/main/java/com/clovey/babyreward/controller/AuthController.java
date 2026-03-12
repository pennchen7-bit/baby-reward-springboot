package com.clovey.babyreward.controller;

import com.clovey.babyreward.dto.*;
import com.clovey.babyreward.entity.Family;
import com.clovey.babyreward.entity.User;
import com.clovey.babyreward.repository.FamilyRepository;
import com.clovey.babyreward.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FamilyRepository familyRepository;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("请输入用户名"));
            }
            
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("请输入密码"));
            }
            
            // 查找用户
            Optional<User> userOpt;
            if (request.getFamilyId() != null && !request.getFamilyId().isEmpty()) {
                // 在指定家庭内查找
                List<User> familyUsers = userRepository.findByFamilyId(request.getFamilyId());
                userOpt = familyUsers.stream()
                    .filter(u -> u.getUsername().equalsIgnoreCase(request.getUsername()))
                    .findFirst();
            } else {
                // 全局查找
                userOpt = userRepository.findByUsername(request.getUsername());
            }
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body(ApiResponse.error("用户名或密码错误"));
            }
            
            User user = userOpt.get();
            
            if (!user.getActive()) {
                return ResponseEntity.status(401).body(ApiResponse.error("账户已被禁用"));
            }
            
            // 验证密码
            if (!BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
                return ResponseEntity.status(401).body(ApiResponse.error("用户名或密码错误"));
            }
            
            // 获取家庭信息
            String familyName = null;
            String familyCode = null;
            if (user.getFamilyId() != null) {
                Optional<Family> familyOpt = familyRepository.findById(user.getFamilyId());
                if (familyOpt.isPresent()) {
                    Family family = familyOpt.get();
                    familyName = family.getName();
                    familyCode = family.getFamilyCode();
                }
            }
            
            UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getFamilyId(),
                familyName,
                familyCode
            );
            
            return ResponseEntity.ok(ApiResponse.success(response));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("登录失败", e.getMessage()));
        }
    }
    
    @PostMapping("/wechat-login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> wechatLogin(@RequestBody WechatLoginRequest request) {
        try {
            if (request.getCode() == null || request.getCode().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("缺少微信 code"));
            }
            
            // 生成 openid（开发模式）
            String openid = "wx_" + request.getCode();
            
            // 检查用户是否已存在
            Optional<User> userOpt = userRepository.findByWechatOpenid(openid);
            
            if (userOpt.isPresent()) {
                // 老用户：直接登录
                User user = userOpt.get();
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("isNewUser", false);
                
                UserResponse userResponse = new UserResponse();
                userResponse.setId(user.getId());
                userResponse.setUsername(user.getUsername());
                userResponse.setRole(user.getRole());
                userResponse.setFamilyId(user.getFamilyId());
                
                if (user.getFamilyId() != null) {
                    Optional<Family> familyOpt = familyRepository.findById(user.getFamilyId());
                    if (familyOpt.isPresent()) {
                        Family family = familyOpt.get();
                        userResponse.setFamilyName(family.getName());
                        userResponse.setFamilyCode(family.getFamilyCode());
                    }
                }
                
                response.put("user", userResponse);
                
                return ResponseEntity.ok(ApiResponse.success(response));
            }
            
            // 新用户：创建用户和家庭
            String familyCode = request.getFamilyCode();
            String role = request.getRole() != null ? request.getRole() : "admin";
            
            if (familyCode != null && !familyCode.isEmpty()) {
                // 有邀请码：加入已有家庭
                Optional<Family> familyOpt = familyRepository.findByFamilyCode(familyCode);
                if (familyOpt.isEmpty()) {
                    return ResponseEntity.status(404).body(ApiResponse.error("家庭码不存在"));
                }
                
                Family family = familyOpt.get();
                String targetRole = "baby".equals(role) ? "baby" : "parent";
                
                // 创建用户
                User newUser = new User();
                newUser.setId("usr_" + System.currentTimeMillis() + "_" + randomString(9));
                newUser.setFamilyId(family.getId());
                newUser.setUsername("微信用户_" + openid.substring(openid.length() - 6));
                newUser.setPasswordHash(BCrypt.hashpw("", BCrypt.gensalt()));
                newUser.setWechatOpenid(openid);
                newUser.setRole(targetRole);
                newUser.setActive(true);
                
                userRepository.save(newUser);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("isNewUser", true);
                
                UserResponse userResponse = new UserResponse(
                    newUser.getId(),
                    newUser.getUsername(),
                    newUser.getRole(),
                    newUser.getFamilyId(),
                    family.getName(),
                    family.getFamilyCode()
                );
                response.put("user", userResponse);
                
                return ResponseEntity.ok(ApiResponse.success(response));
                
            } else {
                // 无邀请码：创建新家庭
                String familyName = "家庭_" + openid.substring(openid.length() - 6);
                String username = "微信用户_" + openid.substring(openid.length() - 6);
                
                // 生成家庭码
                String newFamilyCode = generateFamilyCode();
                
                // 创建家庭
                Family newFamily = new Family();
                newFamily.setId("fam_" + System.currentTimeMillis() + "_" + randomString(9));
                newFamily.setName(familyName);
                newFamily.setFamilyCode(newFamilyCode);
                
                familyRepository.save(newFamily);
                
                // 创建用户
                User newUser = new User();
                newUser.setId("usr_" + System.currentTimeMillis() + "_" + randomString(9));
                newUser.setFamilyId(newFamily.getId());
                newUser.setUsername(username);
                newUser.setPasswordHash(BCrypt.hashpw("", BCrypt.gensalt()));
                newUser.setWechatOpenid(openid);
                newUser.setRole("admin");
                newUser.setActive(true);
                
                userRepository.save(newUser);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("isNewUser", true);
                
                UserResponse userResponse = new UserResponse(
                    newUser.getId(),
                    newUser.getUsername(),
                    newUser.getRole(),
                    newUser.getFamilyId(),
                    newFamily.getName(),
                    newFamily.getFamilyCode()
                );
                response.put("user", userResponse);
                
                Map<String, String> familyInfo = new HashMap<>();
                familyInfo.put("id", newFamily.getId());
                familyInfo.put("name", newFamily.getName());
                familyInfo.put("familyCode", newFamily.getFamilyCode());
                response.put("family", familyInfo);
                
                return ResponseEntity.ok(ApiResponse.success(response));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("微信登录失败", e.getMessage()));
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        // TODO: 从 Cookie 或 Token 获取用户 ID
        return ResponseEntity.ok(ApiResponse.error("未登录"));
    }
    
    private String randomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    private String generateFamilyCode() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            String code = String.valueOf(1000 + random.nextInt(9000));
            if (!familyRepository.existsByFamilyCode(code)) {
                return code;
            }
        }
        return randomString(4).toUpperCase();
    }
}
