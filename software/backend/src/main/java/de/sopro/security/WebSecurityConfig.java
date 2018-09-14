package de.sopro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import de.sopro.model.User;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true, jsr250Enabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
  @Autowired
  DetailsService detailsService;

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception{

    auth.userDetailsService(detailsService).passwordEncoder(User.PASSWORD_ENCODER);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception{
    http.authorizeRequests()
        .anyRequest().permitAll()
        .and()
        .httpBasic()
        .and()
        .csrf().disable();
  }

}