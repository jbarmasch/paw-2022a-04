package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);
    @Autowired
    private UserService userService;

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && !(auth instanceof AnonymousAuthenticationToken);
    }

    public String getUsername() {
        if (isAuthenticated())
            return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        LOGGER.debug("User is not authenticated");
        return null;
    }

    public User getUser() {
        if (isAuthenticated()) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                LOGGER.error("User not found");
                throw new UserNotFoundException();
            }
            return user;
        }

        LOGGER.debug("User is not authenticated");
        return null;
    }

    public Long getUserId() {
        User user = getUser();
        if (user != null)
            return (long) user.getId();
        return null;
    }

    public void refreshAuthorities() {
        User user = getUser();
        if (user == null)
            return;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        if (updatedAuthorities.size() == user.getRoles().size())
            return;
        for (Role role : user.getRoles()) {
            updatedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public boolean isCreator() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && !(auth instanceof AnonymousAuthenticationToken) &&
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));
    }

    public boolean isCreator(User user) {
        return user.getRoles().stream().anyMatch(a -> a.getRoleName().equals("ROLE_CREATOR"));
    }

    public boolean isBouncer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && !(auth instanceof AnonymousAuthenticationToken) &&
                auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BOUNCER"));
    }

    public boolean isEventOwner(Event event) {
        return event.getOrganizer().getId() == getUserId();
    }
}
