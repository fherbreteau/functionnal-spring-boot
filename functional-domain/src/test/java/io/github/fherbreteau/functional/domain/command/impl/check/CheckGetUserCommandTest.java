package io.github.fherbreteau.functional.domain.command.impl.check;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.UUID;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GetUserCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckGetUserCommandTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserChecker userChecker;
    @Mock
    private UserUpdater userUpdater;
    @Mock
    private User actor;

    @Test
    void shouldGenerateGetUserCommandWhenCheckingSucceedForAUserId() {
        UUID userId = UUID.randomUUID();
        CheckGetUserCommand command = new CheckGetUserCommand(userRepository, groupRepository, userChecker,
                userUpdater, null, userId);
        // GIVEN
        given(userRepository.exists(userId)).willReturn(true);
        // WHEN
        Command<Output<User>> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(GetUserCommand.class);
    }

    @Test
    void shouldGenerateGetUserCommandWhenCheckingSucceedForAUserName() {
        CheckGetUserCommand command = new CheckGetUserCommand(userRepository, groupRepository, userChecker,
                userUpdater, "user", null);
        // GIVEN
        given(userRepository.exists("user")).willReturn(true);
        // WHEN
        Command<Output<User>> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(GetUserCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenUserIdNotExists() {
        UUID userId = UUID.randomUUID();
        CheckGetUserCommand command = new CheckGetUserCommand(userRepository, groupRepository, userChecker,
                userUpdater, null, userId);
        // GIVEN
        given(userRepository.exists(userId)).willReturn(false);
        // WHEN
        Command<Output<User>> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenUserNameNotExists() {
        CheckGetUserCommand command = new CheckGetUserCommand(userRepository, groupRepository, userChecker,
                userUpdater, "user", null);
        // GIVEN
        given(userRepository.exists("user")).willReturn(false);
        // WHEN
        Command<Output<User>> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }
}
