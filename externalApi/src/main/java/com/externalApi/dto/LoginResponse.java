package com.externalApi.dto;

import lombok.Data;

@Data
public class LoginResponse {
   private String tokenType;
   private Long expiresIn;
   private String accessToken;

}
