package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.RoleEnum;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class PawUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService us;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = us.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user by the name " + username));
        final Collection<GrantedAuthority> authorities = new ArrayList<>();
         for (Role role : user.getRoles()) {
             authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
         }
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}
