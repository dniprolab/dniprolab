package com.dniprolab.config;

import com.dniprolab.user.repo.UserRepository;
import com.dniprolab.user.security.service.RepositoryUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Created by Overlord on 02.01.2016.
 */
@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    //TODO add roles in process

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                //Spring security will be ignoring requests to static resources such as css and jsp  .
                .ignoring()
                .antMatchers("/static/**");
    }
    
    /*
    * Configures the access to application resources
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Set mapping of logging form
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/authenticate")
                .failureUrl("/login?error=bad_credentials")
                        //Configures the logout function
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                        //Configures url based authorization
                .and()
                .authorizeRequests()
                        //Anyone can access the login url
                .antMatchers(
                        "/auth/**",
                        "/login"
                ).permitAll()
                //The rest of the our application is protected.
                .antMatchers("/**").hasRole("USER");
    }
    /*
    * Set authentication manager which will be process authentication requests
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
        //create access which will always enabled
        auth
                .inMemoryAuthentication()
                .withUser("vladimir").password("putin").roles("USER");
    }

    /*
    * Use BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }


    /*
    * Creates a bean which will be providing access to database
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new RepositoryUserDetailService(userRepository);
    }

    //TODO Create user entity and user service layer

}
