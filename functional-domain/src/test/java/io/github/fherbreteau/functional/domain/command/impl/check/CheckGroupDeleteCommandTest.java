package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GroupDeleteCommand;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckGroupDeleteCommandTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserChecker userChecker;
    private CheckGroupDeleteCommand command;
    @Mock
    private User actor;

    @BeforeEach
    public void setup() {
        command = new CheckGroupDeleteCommand(userRepository, groupRepository, userChecker, "group", false);
    }

    @Test
    void shouldGenerateDeleteGroupCommandWhenCheckingSucceed() {
        // GIVEN
        given(userChecker.canDeleteGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(userRepository.hasUserWithGroup("group")).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(GroupDeleteCommand.class);
    }

    @Test
    void shouldGenerateDeleteForceGroupCommandWhenCheckingSucceed() {
        // GIVEN
        given(userChecker.canDeleteGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(userRepository.hasUserWithGroup("group")).willReturn(true);
        // WHEN
        command = new CheckGroupDeleteCommand(userRepository, groupRepository, userChecker, "group", true);
        Command<Output> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(GroupDeleteCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(userChecker.canDeleteGroup("group", actor)).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenGroupNameExists() {
        // GIVEN
        given(userChecker.canDeleteGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenUserInGroup() {
        // GIVEN
        given(userChecker.canDeleteGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(userRepository.hasUserWithGroup("group")).willReturn(true);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }
}
