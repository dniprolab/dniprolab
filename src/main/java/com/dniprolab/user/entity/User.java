package com.dniprolab.user.entity;

import javax.persistence.*;

/**
 * Created by Overlord on 02.01.2016.
 */
@Entity
@Table(name = "USER_ACCOUNTS")
public class User extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "LOGIN", length = 100, nullable = false, unique = true)
    private String login;

    @Column(name = "PASSWORD", length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", length = 55, nullable = false)
    private Role role;

//    private AccountData; TODO add class for user description

    @Override
    public Long getId() {
        return id;
    }

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
