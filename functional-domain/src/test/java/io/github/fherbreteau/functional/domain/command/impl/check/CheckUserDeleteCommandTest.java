package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.UserDeleteCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckUserDeleteCommandTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserChecker userChecker;
    @Mock
    private PasswordProtector passwordProtector;
    private CheckUserDeleteCommand command;
    @Mock
    private User actor;

    @BeforeEach
    public void setup() {
        command = new CheckUserDeleteCommand(userRepository, groupRepository, userChecker, passwordProtector, "user");
    }

    @Test
    void shouldGenerateDeleteUserCommandWhenCheckingSucceed() {
        // GIVEN
        given(userChecker.canDeleteUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(true);
        // WHEN
        Command<Output> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(UserDeleteCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(userChecker.canDeleteUser("user", actor)).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenGroupNameExists() {
        // GIVEN
        given(userChecker.canDeleteUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }
}
