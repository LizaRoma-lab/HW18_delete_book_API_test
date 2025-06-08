package com.demoqa.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginBodyModel {
    String userName;
    String password;
}
