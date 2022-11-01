package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.service.UserService;
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
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:authentication.properties")
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PawUserDetailsService userDetailsService;
    @Autowired
    private Environment env;
    @Autowired
    private UserService userService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/register", "/forgot-pass").permitAll()
                .antMatchers("/users/**").anonymous()
                .antMatchers("/stats").hasRole("CREATOR")
                .antMatchers("/my-events").hasRole("CREATOR")
                .antMatchers(HttpMethod.POST, "/events/*").authenticated()
                .antMatchers(HttpMethod.POST, "/events").permitAll()
                .antMatchers(HttpMethod.GET, "/events", "/events/*").permitAll()
                .antMatchers(HttpMethod.POST, "/bookings/*/confirm").hasRole("BOUNCER")
                .antMatchers(HttpMethod.POST, "/bookings/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/bookings/**").authenticated()
                .antMatchers(HttpMethod.GET, "/profile/**").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/search", "/events/*", "/profile/**").not().hasAnyRole("BOUNCER")
                .antMatchers("/**").hasAnyRole("CREATOR", "USER")
                .and()
                .formLogin()
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

//    public boolean checkProfile(Authentication authentication, int userId) {
//        User profile = userService.getUserById(userId).orElse(null);
//        if (profile != null) {
//            if (profile.getRoles().stream().anyMatch(a -> a.getRoleName().equals("ROLE_CREATOR"))) {
//                return true;
//            }
//        }
//        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
//            return false;
//        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
//        User user = userService.findByUsername(username).orElse(null);
//        if (user != null)
//            return user.getId() == userId;
//        return false;
//    }
}
