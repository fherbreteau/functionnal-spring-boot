package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GroupGetCommandTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private PasswordProtector passwordProtector;
    @Mock
    private User actor;

    @Test
    void shouldGetGroupsOfAUserById() {
        UUID userId = UUID.randomUUID();
        User user = User.builder("user").withUserId(userId).withGroup(Group.builder("group").build()).build();
        given(userRepository.findById(userId)).willReturn(user);

        Command<Output> executeCommand = new GroupGetCommand(userRepository, groupRepository, passwordProtector,
                null, userId);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, list(Group.class))
                .isNotNull()
                .hasSize(1)
                .first()
                .extracting(Group::getName)
                .isEqualTo("group");
    }

    @Test
    void shouldGetGroupsOfActor() {
        given(actor.getGroups()).willReturn(List.of(Group.builder("group").build()));

        Command<Output> executeCommand = new GroupGetCommand(userRepository, groupRepository, passwordProtector,
                null, null);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, list(Group.class))
                .isNotNull()
                .hasSize(1)
                .first()
                .extracting(Group::getName)
                .isEqualTo("group");
    }
}
