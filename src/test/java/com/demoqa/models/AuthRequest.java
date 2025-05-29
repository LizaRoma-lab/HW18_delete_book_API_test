package com.demoqa.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthRequest {
    String userId;
    String userName;
    String password;
    String token;
    String expires;
    @JsonProperty("created_date")
    String createdDate;
    String isActive;
}
