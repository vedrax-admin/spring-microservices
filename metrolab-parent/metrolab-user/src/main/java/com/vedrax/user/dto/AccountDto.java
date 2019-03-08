package com.vedrax.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author remypenchenat
 */
public class AccountDto {

    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min= 4, max = 15)
    private String password;
    @NotNull
    @Size(min= 4, max = 60)
    private String fullName;
    @NotNull
    private String securityRole;

    public AccountDto() {
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSecurityRole() {
        return securityRole;
    }

    public void setSecurityRole(String securityRole) {
        this.securityRole = securityRole;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.email)
                .append(this.password)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AccountDto object = (AccountDto) obj;
        return new EqualsBuilder()
                .append(this.email, object.email)
                .append(this.password, object.password)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }

}
