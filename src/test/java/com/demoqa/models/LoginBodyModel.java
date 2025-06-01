package com.demoqa.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginBodyModel {
    String userName;
    String password;
}
