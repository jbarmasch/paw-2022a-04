package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true
)
@PropertySource("classpath:authentication.properties")
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PawUserDetailsService userDetailsService;
    @Autowired
    private Environment env;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private AuthenticationTokenService authenticationTokenService;
    @Autowired
    private UserService userService;
//    @Autowired
//    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.addFilterBefore(new CorsResponseFilter(), ChannelProcessingFilter.class);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl().disable()
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
//                .antMatchers("/login", "/register", "/forgot-pass").permitAll()
//                .antMatchers("/stats").hasRole("CREATOR")
//                .antMatchers("/my-events").hasRole("CREATOR")
                .antMatchers(HttpMethod.POST, "/api/events").hasAnyRole("CREATOR", "USER")
                .antMatchers(HttpMethod.GET, "/api/events", "/api/events/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/events/few-tickets").permitAll()
                .antMatchers(HttpMethod.GET, "/api/events/*/tickets").permitAll()
                .antMatchers(HttpMethod.GET, "/api/events/upcoming").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/test").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/api/users", "/api/users/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users", "/api/users/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/*/event-stats").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/*/stats").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/*/ticket-stats").permitAll()
                .antMatchers(HttpMethod.GET, "/api/locations", "/api/locations/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/bookings", "/api/bookings/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/tags", "/api/tags/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/types", "/api/types/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/image", "/api/image/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/tickets", "/api/tickets/*").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/tickets/*").hasRole("CREATOR")
                .antMatchers(HttpMethod.POST, "/api/tickets").hasRole("CREATOR")
//                .antMatchers(HttpMethod.PUT, "/api/tickets/*").hasRole("CREATOR")
//                .antMatchers(HttpMethod.POST, "/bookings/*/confirm").hasRole("BOUNCER")
//                .antMatchers(HttpMethod.POST, "/bookings/**").hasRole("USER")
//                .antMatchers(HttpMethod.GET, "/bookings/**").authenticated()
//                .antMatchers(HttpMethod.GET, "/profile/**").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/search", "/events/*", "/profile/**").not().hasAnyRole("BOUNCER")
//                .anyRequest().authenticated()
                .antMatchers("/**").permitAll()
//                .and().exceptionHandling()
                .and().exceptionHandling().authenticationEntryPoint(new BasicAuthenticationEntryPoint())
//                .accessDeniedPage("/403")
//                .and().addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .and().csrf().disable()
                .addFilterBefore(new JwtAuthenticationTokenFilter(authenticationManagerBean(), authenticationTokenService, userService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/resources/css/**", "/resources/js/**", "/resources/svg/**", "/resources/png/**", "/favicon.ico", "/403");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public CorsConfiguration corsConfiguration() {
//        CorsConfiguration cors = new CorsConfiguration();
//        cors.addAllowedOrigin("*");
//        return cors;
//    }
}
