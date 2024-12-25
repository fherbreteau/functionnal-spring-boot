package io.github.fherbreteau.functional.domain.command.impl.check;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GetGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Group;
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
class CheckGetGroupCommandTest {
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
    void shouldGenerateGetGroupCommandWhenCheckingSucceedForAUserId() {
        UUID userId = UUID.randomUUID();
        CheckGetGroupCommand command = new CheckGetGroupCommand(userRepository, groupRepository, userChecker,
                userUpdater, null, userId);
        // GIVEN
        given(userRepository.exists(userId)).willReturn(true);
        // WHEN
        Command<Output<List<Group>>> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(GetGroupCommand.class);
    }

    @Test
    void shouldGenerateGetGroupCommandWhenCheckingSucceedForAUserName() {
        CheckGetGroupCommand command = new CheckGetGroupCommand(userRepository, groupRepository, userChecker,
                userUpdater, "user", null);
        // GIVEN
        given(userRepository.exists("user")).willReturn(true);
        // WHEN
        Command<Output<List<Group>>> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(GetGroupCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenUserIdNotExists() {
        UUID userId = UUID.randomUUID();
        CheckGetGroupCommand command = new CheckGetGroupCommand(userRepository, groupRepository, userChecker,
                userUpdater, null, userId);
        // GIVEN
        given(userRepository.exists(userId)).willReturn(false);
        // WHEN
        Command<Output<List<Group>>> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenUserNameNotExists() {
        CheckGetGroupCommand command = new CheckGetGroupCommand(userRepository, groupRepository, userChecker,
                userUpdater, "user", null);
        // GIVEN
        given(userRepository.exists("user")).willReturn(false);
        // WHEN
        Command<Output<List<Group>>> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }
}
