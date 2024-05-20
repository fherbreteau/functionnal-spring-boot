package io.github.fherbreteau.functional.domain.user;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;
import io.github.fherbreteau.functional.exception.NotFoundException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserManagerTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;

    private UserManager userManager;

    @BeforeEach
    public void setup() {
        userManager = new UserManager(userRepository, groupRepository);
    }

    @Test
    void shouldLookupUserByName() {
        // GIVEN
        User user = User.builder("user").build();
        given(userRepository.findByName("user")).willReturn(user);
        // WHEN
        Output result = userManager.findUserByName("user");
        // THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        assertThat(result)
                .extracting(Output::getValue)
                .isEqualTo(user);
    }

    @Test
    void shouldReturnUnErrorWhenUserLookupFails() {
        // GIVEN
        given(userRepository.findByName("user")).willThrow(new NotFoundException("user"));
        // WHEN
        Output result = userManager.findUserByName("user");
        // THEN
        assertThat(result).isNotNull()
                .extracting(Output::isError, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        assertThat(result)
                .extracting(Output::getError, type(Error.class))
                .extracting(Error::getMessage)
                .isEqualTo("user not found");
    }

    @Test
    void shouldLookupGroupByName() {
        // GIVEN
        Group group = Group.builder("group").build();
        given(groupRepository.findByName("group")).willReturn(group);
        // WHEN
        Output result = userManager.findGroupByName("group");
        // THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        assertThat(result)
                .extracting(Output::getValue)
                .isEqualTo(group);
    }

    @Test
    void shouldReturnUnErrorWhenGroupLookupFails() {
        // GIVEN
        given(groupRepository.findByName("group")).willThrow(new NotFoundException("group", new Exception()));
        // WHEN
        Output result = userManager.findGroupByName("group");
        // THEN
        assertThat(result).isNotNull()
                .extracting(Output::isError, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        assertThat(result)
                .extracting(Output::getError, type(Error.class))
                .extracting(Error::getMessage)
                .isEqualTo("group not found");
    }

    @Test
    void shouldCheckUserPassword() {
        // GIVEN
        User user = User.builder("user").build();
        given(userRepository.findByName("user")).willReturn(user);
        given(userRepository.checkPassword(user, "password")).willReturn(true);
        // WHEN
        boolean result = userManager.checkPassword("user", "password");
        // THEN
        assertThat(result).isTrue();
    }

    @Test
    void shouldCheckUserPassword1() {
        // GIVEN
        User user = User.builder("user").build();
        given(userRepository.findByName("user")).willReturn(user);
        given(userRepository.checkPassword(user, "password")).willReturn(false);
        // WHEN
        boolean result = userManager.checkPassword("user", "password");
        // THEN
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseWhenUserNotFound() {
        // GIVEN
        given(userRepository.findByName("user")).willThrow(new NotFoundException("user"));
        // WHEN
        boolean result = userManager.checkPassword("user", "password");
        // THEN
        assertThat(result).isFalse();
    }
}
