package com.gl.testngfw.report.model;

import com.gl.testngfw.enums.Authority;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String loginName;
    private String fullName;
    private String password;
    //private List<Role> role;
    private String AccessToken;
    private String JwtToken;
    private List<String> projectUniqueKey;
    private List<Authority> authorities;

    public User() {
        super();
    }


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        this.AccessToken = accessToken;
    }

    public List<String> getProjectUniqueKey() {
        return projectUniqueKey;
    }

    public void setProjectUniqueKey(List<String> projectUniqueKey) {
        this.projectUniqueKey = projectUniqueKey;
    }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
