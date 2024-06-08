package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetUserCommandTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private User actor;

    @Test
    void shouldGetAUserById() {
        UUID userId = UUID.randomUUID();
        User user = User.builder("user").withUserId(userId).build();
        given(userRepository.findById(userId)).willReturn(user);

        Command<Output<User>> executeCommand = new GetUserCommand(userRepository, groupRepository,
                null, userId);
        Output<User> output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getName)
                .isEqualTo("user");
    }

    @Test
    void shouldGetActor() {
        Command<Output<User>> executeCommand = new GetUserCommand(userRepository, groupRepository,
                null, null);
        Output<User> output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .isEqualTo(actor);
    }
}
