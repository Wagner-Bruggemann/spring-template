package com.namgrengaw.template.application.core.domain.security;

import java.io.Serializable;
import java.util.Objects;

public class AccountCredentials implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String name;
    private String password;

    public AccountCredentials() {
    }

    public AccountCredentials(String username, String fullname, String password) {
        this.username = username;
        this.name = fullname;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String fullname) {
        this.name = fullname;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        AccountCredentials that = (AccountCredentials) object;
        return Objects.equals(username, that.username) && Objects.equals(name, that.name) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, password);
    }
}
