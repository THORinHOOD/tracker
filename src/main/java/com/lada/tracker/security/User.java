package com.lada.tracker.security;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private String login;
    private String password;
    private List<String> roles;

}
