package de.sopro.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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
    http.cors()
        .and().authorizeRequests()
        .anyRequest().permitAll()
        .and()
        .httpBasic().and().logout().logoutSuccessHandler(new LogoutSuccessHandlerImpl())
        .and().csrf().disable();
  }

  private class LogoutSuccessHandlerImpl implements LogoutSuccessHandler{

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
        response.setStatus(200);
		
	}
    
  }


}
