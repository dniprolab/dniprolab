package com.dniprolab.user.security.service;

import com.dniprolab.user.entity.User;
import com.dniprolab.user.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Overlord on 03.01.2016.
 */

/*
* This class load user from JPA repository, create and return org.springframework.security.core.userdetails.User
 */
public class RepositoryUserDetailService implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(RepositoryUserDetailService.class);

    private UserRepository userRepository;

    @Autowired
    public RepositoryUserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /*
    * Load user from database using JPA
    * @param user login
    * @throws UserNotFoundException if user is absent
    * @return User dto for security layer
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        User user = userRepository.findUserByLogin(login);

        logger.debug("Founded user: {}" + user);

        if(user == null){
            throw new UsernameNotFoundException("User with name: " + login + " is absent.");
        }

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        authorities.add(authority);

        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(
            user.getLogin(), user.getPassword(), authorities
        );

        logger.debug("Security layer user " + principal + " added to security context");

        return principal;
    }
}
