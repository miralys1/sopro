package de.sopro.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.sopro.model.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Conifiguration class that defines the behavior of the framework and its security.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true, jsr250Enabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
  



  @Autowired
  UserService userService;

  /**
   * Creates a passwordencoder so the password are not saved as cleartext
   *
   * @return the created passwordencoder
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  /**
   * Creates the Authenticationprovider, which determines if a correct
   * password-user-pair is used and therefore the user is autheticated
   *
   * @return
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
    auth.setUserDetailsService(userService);
    auth.setPasswordEncoder(passwordEncoder());
    return auth;
  }

  /**
   * Sets the autenticationsprovider for the authenticationManagerBulder
   * @param  auth the AuthenticationManagerBuilder in use
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth ){
    auth.authenticationProvider(authenticationProvider());
  }


  /**
   * Method to define the access of certain URLs, authentication and the behavior on logout
   * @param http
   * @throws Exception
   */
  @Override
  public void configure(HttpSecurity http) throws Exception{
    http.cors()
            // all URLs are permitted, the controller handle the access
        .and().authorizeRequests()
        .anyRequest().permitAll()
        .and()
            // basic authentication enabled
        .httpBasic()
        .and()
            // define custom handler to define behavior at success
        .logout().logoutSuccessHandler(new LogoutSuccessHandlerImpl())
        .and()
        .csrf().disable();
  }

  /**
   * Small class to define Logout behavior.
   * In case of successful logout simply return Success-status.
   * Gets rid of redirect, which is unwanted, since the single page application does the routing.
   */
  private class LogoutSuccessHandlerImpl implements LogoutSuccessHandler{

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
        response.setStatus(200);
		
	}
    
  }


}
