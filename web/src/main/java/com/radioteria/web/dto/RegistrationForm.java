package com.radioteria.web.dto;

import com.radioteria.web.security.ValidEmail;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class RegistrationForm {
    @NotNull
    @ValidEmail
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setName(String name) {
        this.name = name;
    }
}
