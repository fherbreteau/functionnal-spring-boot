package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.UserAddCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckUserAddCommandTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserChecker userChecker;
    private CheckUserAddCommand command;
    @Mock
    private User actor;

    @BeforeEach
    public void setup() {
        command = new CheckUserAddCommand(userRepository, groupRepository, userChecker, "user", null, null, null);
    }

    @Test
    void shouldGenerateCreateUserCommandWhenCheckingSucceed() {
        // GIVEN
        given(userChecker.canCreateUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(false);
        given(userRepository.exists("user")).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(UserAddCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(userChecker.canCreateUser("user", actor)).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenUserNameExists() {
        // GIVEN
        given(userChecker.canCreateUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(true);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenGroupNameExists() {
        // GIVEN
        given(userChecker.canCreateUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(false);
        given(groupRepository.exists("user")).willReturn(true);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenUserIdExists() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        given(userChecker.canCreateUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(false);
        given(userRepository.exists(userId)).willReturn(true);
        // WHEN
        command = new CheckUserAddCommand(userRepository, groupRepository, userChecker, "user", userId, groupId, null);
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenGroupIdNotExists() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        given(userChecker.canCreateUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(false);
        given(userRepository.exists(userId)).willReturn(false);
        given(groupRepository.exists(groupId)).willReturn(false);
        // WHEN
        command = new CheckUserAddCommand(userRepository, groupRepository, userChecker, "user", userId, groupId, null);
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }
}
