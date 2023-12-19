package com.petshop.models.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petshop.common.constant.Role;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.sql.Date;
import java.util.Collection;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long UserId;
    @Column(name = "user_name", nullable = false)
    private String UserName;
    @Column(name = "password", nullable = false)
    private String Password;
    @Column(name = "firstname", nullable = false)
    private String FirstName;
    @Column(name = "lastname", nullable = false)
    private String LastName;
    @Column(name = "phone_number", nullable = false)
    private String PhoneNumber;
    @Column(name = "date_of_birth")
    private Date DateOfBirth;
    @Column(name = "email", nullable = false)
    private String Email;
    @Column(name = "address")
    private String Address;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role Role;
    @Column(name = "status", columnDefinition = "INT DEFAULT 1")
    private int Status = 1;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Token> tokens;
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<FeedBack> feedBacks;


    public User(String UserName, String Password) {
        this.UserName = UserName;
        this.Password = Password;
    }

    public User( String UserName, String Password, String FirstName, String LastName, String PhoneNumber, Date DateOfBirth, String Email, String Address) {
        this.UserName = UserName;
        this.Password = Password;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.PhoneNumber = PhoneNumber;
        this.DateOfBirth = DateOfBirth;
        this.Email = Email;
        this.Address = Address;
        Role = com.petshop.common.constant.Role.customer;
        Status = 1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Role.name()));
    }

    @Override
    public String getPassword() {
        return Password;
    }


    @Override
    public String getUsername() {
        return UserName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
