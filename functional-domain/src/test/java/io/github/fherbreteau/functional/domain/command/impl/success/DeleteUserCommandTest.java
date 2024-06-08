package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteUserCommandTest {
    private DeleteUserCommand command;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserUpdater userUpdater;
    @Mock
    private User actor;

    private UUID userId;

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
        command = new DeleteUserCommand(userRepository, groupRepository, userUpdater, "user");
    }

    @Test
    void shouldDeleteGroupWhenExecutingCommand() {
        // GIVEN
        User user = User.builder("user").withUserId(userId).build();
        given(userRepository.findByName("user")).willReturn(user);
        // WHEN
        Output<Void> result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(userRepository).delete(user);
        verify(userUpdater).deleteUser(user);
    }
}
