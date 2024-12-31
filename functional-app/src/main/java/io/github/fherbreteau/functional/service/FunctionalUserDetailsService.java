package io.github.fherbreteau.functional.service;

import static org.springframework.security.core.userdetails.User.builder;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driving.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class FunctionalUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public FunctionalUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Cacheable
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Output<User> output = userService.findUserByName(username);
        if (output.isFailure()) {
            throw new UsernameNotFoundException(output.getFailure().getMessage());
        }
        User user = output.getValue();
        Output<String> password = userService.getUserPassword(user);
        if (password.isFailure()) {
            throw new UsernameNotFoundException(password.getFailure().getMessage());
        }
        String[] roles = user.getGroups().stream()
                .map(Group::getName)
                .map(String::toUpperCase)
                .toArray(String[]::new);
        return builder()
                .username(user.getName())
                .password(password.getValue())
                .roles(roles)
                .build();
    }
}
