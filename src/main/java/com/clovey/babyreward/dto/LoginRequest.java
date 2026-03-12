package com.clovey.babyreward.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
    
    private String username;
    private String password;
    private String familyCode;
    private String familyId;
}
