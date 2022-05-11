package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.auth.RefererRedirectionAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:authentication.properties")
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PawUserDetailsService userDetailsService;
    @Autowired
    private Environment env;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                .and().authorizeRequests()
                    .antMatchers("/login", "/register", "/forgot-pass").permitAll()
                    .antMatchers("/stats").hasRole("CREATOR")
                    .antMatchers( "/events", "/", "/search").permitAll()
                    .antMatchers(HttpMethod.GET, "/events/**/add-tickets").authenticated()
                    .antMatchers(HttpMethod.GET, "/events/*").permitAll()
                    .antMatchers(HttpMethod.GET, "/events/*/**").authenticated()
                    .antMatchers(HttpMethod.GET, "/profile").authenticated()
                    .antMatchers(HttpMethod.GET, "/profile/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/events/**").authenticated()
                    .antMatchers("/**").authenticated()
                .and().formLogin()
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    .successHandler(successHandler())
                    .loginPage("/login")
                .and().rememberMe()
                    .rememberMeParameter("j_rememberme")
                    .userDetailsService(userDetailsService)
                    .key(env.getProperty("remember_key"))
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .and().logout()
                    .logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/")
                .and().exceptionHandling()
                    .accessDeniedPage("/403")
                .and().csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/resources/css/**", "/resources/js/**", "/resources/svg/**", "/resources/png/**", "/favicon.ico", "/403", "/image/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new RefererRedirectionAuthentication("/");
    }
}