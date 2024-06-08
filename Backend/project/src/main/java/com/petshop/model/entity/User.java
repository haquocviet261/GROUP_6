package com.petshop.model.entity;

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
    private Long user_id;
    @Column(name = "user_name", nullable = false)
    private String user_name;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "firstname", nullable = false)
    private String first_name;
    @Column(name = "lastname", nullable = false)
    private String last_name;
    @Column(name = "phone_number")
    private String phone_number;
    @Column(name = "date_of_birth")
    private Date date_of_birth;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "address")
    private String address;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Column(name = "status", columnDefinition = "INT DEFAULT 1")
    private int status = 1;
    @Column(name = "images_src")
    private String image_src;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Token> tokens;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<FeedBack> feedBacks;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "sender",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<Conversation> sentConversations;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "receiver",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<Conversation> receivedConversations;
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private OnlineStatus online_status;
    public User(String UserName, String Password) {
        this.user_name = UserName;
        this.password = Password;

    }

    public User(String UserName, String Password, String FirstName, String last_name, String phone_number, Date date_of_birth, String Email, String Address) {
        this.user_name = UserName;
        this.password = Password;
        this.first_name = FirstName;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.date_of_birth = date_of_birth;
        this.email = Email;
        this.address = Address;
        role = Role.customer;
        status = 1;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

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
