package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.AuthenticationTokenService;
import ar.edu.itba.paw.webapp.auth.JwtAuthenticationProvider;
import ar.edu.itba.paw.webapp.auth.JwtAuthenticationTokenFilter;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
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
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
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

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.addFilterBefore(new ResponseFilter(env), ChannelProcessingFilter.class);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().cacheControl().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/events").hasAnyRole("USER", "CREATOR")
                .antMatchers(HttpMethod.PUT, "/api/events/**").hasAnyRole("CREATOR")
                .antMatchers(HttpMethod.PUT, "/api/events/*/image").hasAnyRole("CREATOR")
                .antMatchers(HttpMethod.POST, "/api/events/*/rating").hasAnyRole("CREATOR", "USER")
                .antMatchers(HttpMethod.PATCH, "/api/events/*").hasAnyRole("CREATOR")
                .antMatchers(HttpMethod.DELETE, "/api/events/*").hasRole("CREATOR")
                .antMatchers(HttpMethod.POST, "/api/events/*/tickets").hasAnyRole("CREATOR")
                .antMatchers(HttpMethod.POST, "/api/events/*/bookings").hasRole("CREATOR")
                .antMatchers(HttpMethod.GET, "/api/events/*/stats").hasAnyRole("CREATOR")
                .antMatchers(HttpMethod.GET, "/api/events/*/ticket-stats").hasAnyRole("CREATOR")
                .antMatchers(HttpMethod.GET, "/api/bookings/**").hasAnyRole("CREATOR", "USER", "BOUNCER")
                .antMatchers(HttpMethod.PATCH, "/api/bookings/*").hasRole("BOUNCER")
                .antMatchers(HttpMethod.DELETE, "/api/bookings/*").hasAnyRole("CREATOR", "USER")
                .antMatchers(HttpMethod.GET, "/api/organizers/*/stats").hasRole("CREATOR")
                .antMatchers(HttpMethod.DELETE, "/api/tickets/*").hasRole("CREATOR")
                .antMatchers(HttpMethod.PUT, "/api/tickets/*").hasRole("CREATOR")
                .antMatchers(HttpMethod.POST, "/api/tickets").hasRole("CREATOR")
                .antMatchers(HttpMethod.GET, "/api/users/*/ticket-bookings").hasAnyRole("CREATOR", "USER")
                .antMatchers(HttpMethod.GET, "/api/users/*/stats").hasAnyRole("CREATOR", "USER")
                .antMatchers("/**").permitAll()
                .and().exceptionHandling()
                .authenticationEntryPoint(new BasicAuthenticationEntryPoint())
                .and()
                .csrf().disable()
                .addFilterBefore(new JwtAuthenticationTokenFilter(authenticationManagerBean(), authenticationTokenService, userService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
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
}
