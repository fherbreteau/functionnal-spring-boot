package io.github.fherbreteau.functional.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.collection;
import static org.mockito.Mockito.when;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driving.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class FunctionUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private FunctionalUserDetailsService service;

    @Test
    void shouldLoadUserByUsername() {
        // Arrange
        User user = User.builder("username").build();
        when(userService.findUserByName("username"))
                .thenReturn(Output.success(user));
        when(userService.getUserPassword(user))
                .thenReturn(Output.success("password"));

        // Act
        UserDetails result = service.loadUserByUsername("username");

        // Assert
        assertThat(result)
                .extracting(UserDetails::getUsername, UserDetails::getPassword)
                .containsExactly("username", "password");
        assertThat(result)
                .extracting(UserDetails::getAuthorities, collection(GrantedAuthority.class))
                .singleElement()
                .extracting(GrantedAuthority::getAuthority)
                .isEqualTo("ROLE_USERNAME");
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        // Arrange
        when(userService.findUserByName("username"))
                .thenReturn(Output.failure("user not found"));

        // Act
        assertThatThrownBy(() -> service.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("user not found");
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenPasswordNotFound() {
        // Arrange
        User user = User.builder("username").build();
        when(userService.findUserByName("username"))
                .thenReturn(Output.success(user));
        when(userService.getUserPassword(user))
                .thenReturn(Output.failure("password not found"));

        // Act
        assertThatThrownBy(() -> service.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("password not found");
    }
}
