package com.dniprolab.user.security.service;

import com.dniprolab.user.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Overlord on 03.01.2016.
 */
public class RepositoryUserDetailService implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(RepositoryUserDetailService.class);

    private UserRepository userRepository;

    @Autowired
    public RepositoryUserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
        //TODO implement method
    }
}
