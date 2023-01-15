package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.RoleEnum;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PawUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PawUserDetailsService.class);
//    @Autowired
//    private UserService us;
//
//    @Override
//    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
//        final User user = us.findByUsername(username).orElse(null);
//        if (user == null) {
//            LOGGER.error("No user by the name {}", username);
//            throw new UsernameNotFoundException("Username not found");
//        }
//
//        final Collection<GrantedAuthority> authorities = new ArrayList<>();
//        for (Role role : user.getRoles())
//            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
//        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
//    }

    @Autowired
    private UserService us;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = us.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }

        return new AuthenticatedUserDetails.Builder()
                .withUsername(user.getUsername())
                .withPassword(user.getPassword())
                .withAuthorities(mapToGrantedAuthorities(user.getRoles()))
                .withActive(true)
                .build();
    }

    private Set<GrantedAuthority> mapToGrantedAuthorities(List<Role> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRoleName()))
                .collect(Collectors.toSet());
    }
}
