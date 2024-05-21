package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.GroupModifyCommand;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckGroupModifyCommandTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserChecker userChecker;
    @Mock
    private PasswordProtector passwordProtector;
    private CheckGroupModifyCommand command;
    @Mock
    private User actor;

    @BeforeEach
    public void setup() {
        command = new CheckGroupModifyCommand(userRepository, groupRepository, userChecker, passwordProtector,
                "group", null, "group1");
    }

    @Test
    void shouldGenerateModifyGroupCommandWhenCheckingSucceed() {
        // GIVEN
        given(userChecker.canUpdateGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(groupRepository.exists("group1")).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(GroupModifyCommand.class);
    }

    @Test
    void shouldGenerateModifyGroupCommandWhenCheckingSucceed1() {
        // GIVEN
        UUID groupId = UUID.randomUUID();
        given(userChecker.canUpdateGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(groupRepository.exists(groupId)).willReturn(false);
        // WHEN
        command = new CheckGroupModifyCommand(userRepository, groupRepository, userChecker, passwordProtector,
                "group", groupId, null);
        Command<Output> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(GroupModifyCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(userChecker.canUpdateGroup("group", actor)).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenGroupDontExists() {
        // GIVEN
        given(userChecker.canUpdateGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenGroupNameExists() {
        // GIVEN
        given(userChecker.canUpdateGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(groupRepository.exists("group1")).willReturn(true);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenGroupIdExists() {
        // GIVEN
        UUID groupId = UUID.randomUUID();
        given(userChecker.canUpdateGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(groupRepository.exists(groupId)).willReturn(true);
        // WHEN
        command = new CheckGroupModifyCommand(userRepository, groupRepository, userChecker, passwordProtector,
                "group", groupId, null);
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(UserErrorCommand.class);
    }
}
