package com.dniprolab.user.security.dto;

import com.dniprolab.user.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Overlord on 03.01.2016.
 */
public class DniprolabUserDetails {

    private String login;

    private String password;

    private Role role;

    private Set<GrantedAuthority> authorities;

    public DniprolabUserDetails(String login, String password, Role role){
        this.login = login;
        this.password = password;
        this.role = role;
        authorities = new HashSet<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
        this.authorities.add(authority);
    }

    public DniprolabUserDetails() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "DniprolabUserDetails{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", authorities=" + authorities +
                '}';
    }
}
