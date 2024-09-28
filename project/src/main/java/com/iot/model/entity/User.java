package com.iot.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User extends CommonEntity implements UserDetails {
    @Column(name = "user_name")
    private String user_name;
    @Column(name = "password")
    private String password;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "phone_number")
    private String phone_number;
    @Column(name = "date_of_birth")
    private Date date_of_birth;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "role")
    private String role;
    @Column(name = "status")
    private String status;
    @Column(name = "images_src")
    private String images_src;

    public User(String UserName, String Password) {
        this.user_name = UserName;
        this.password = Password;
    }

    public User(String UserName, String Password, String FirstName, String lastname, String phone_number, Date date_of_birth, String email, String address) {
        this.user_name = UserName;
        this.password = Password;
        this.firstname = FirstName;
        this.lastname = lastname;
        this.phone_number = phone_number;
        this.date_of_birth = date_of_birth;
        this.email = email;
        this.address = address;
        this.role = "USER";
        status = "ACTIVE";
    }

    public User(String UserName, String FirstName, String lastname, String phone_number, Date date_of_birth, String Email, String Address) {
        this.user_name = UserName;
        this.firstname = FirstName;
        this.lastname = lastname;
        this.phone_number = phone_number;
        this.date_of_birth = date_of_birth;
        this.email = Email;
        this.address = Address;
        this.role = "USER";
        status = "ACTIVE";
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return user_name;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
