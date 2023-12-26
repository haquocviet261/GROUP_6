package com.petshop.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Set;

@RequiredArgsConstructor
public enum Role {
    customer,
    admin;
    //test gitcommit

}
