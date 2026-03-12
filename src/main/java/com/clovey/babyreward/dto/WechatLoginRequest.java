package com.clovey.babyreward.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WechatLoginRequest {
    
    private String code;
    private String familyCode;
    private String role;
}
