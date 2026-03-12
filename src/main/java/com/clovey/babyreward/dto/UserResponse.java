package com.clovey.babyreward.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private String id;
    private String username;
    private String role;
    private String familyId;
    private String familyName;
    private String familyCode;
}
