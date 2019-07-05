package org.library.security.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.inject.Inject;

@EnableWebSecurity
@ComponentScan(basePackages = {"org.library"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Inject
    AuthenticationProvider authenticationProvider;


    private Logger logger = Logger.getLogger(WebSecurityConfig.class);

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("http: " + http);
        http.authorizeRequests().antMatchers("/").authenticated()
                .and()
                .authorizeRequests().antMatchers("/user**").authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .authorizeRequests().antMatchers("/password**").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

}
