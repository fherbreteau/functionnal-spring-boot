package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CompositeUserCommandFactory;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.user.UserManager;
import io.github.fherbreteau.functional.driving.impl.UserServiceImpl;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;
    @Mock
    private UserManager userManager;
    @Mock
    private CompositeUserCommandFactory commandFactory;
    @Mock
    private CheckCommand<Output> checkCommand;
    @Mock
    private Command<Output> executeCommand;
    @Mock
    private User actor;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userManager, commandFactory);
    }

    @Test
    void findUserByNameShouldDelegateToUserService() {
        // GIVEN
        given(userManager.findUserByName("name")).willReturn(new Output("name"));
        // WHEN
        Output result = userService.findUserByName("name");
        // THEN
        assertThat(result).extracting(Output::isSuccess, BOOLEAN)
                .isTrue();
    }

    @Test
    void findGroupByNameShouldDelegateToUserService() {
        // GIVEN
        given(userManager.findGroupByName("name")).willReturn(new Output("name"));
        // WHEN
        Output result = userService.findGroupByName("name");
        // THEN
        assertThat(result).extracting(Output::isSuccess, BOOLEAN)
                .isTrue();
    }

    @Test
    void testProcessKnownCommandShouldOutputAResult() {
        // Given
        given(commandFactory.createCommand(any(), any())).willReturn(checkCommand);
        given(checkCommand.execute(actor)).willReturn(executeCommand);
        given(executeCommand.execute(actor)).willReturn(new Output(new Object()));
        // When
        Output result = userService.processCommand(UserCommandType.USERADD, actor, UserInput.builder("user").build());
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess)
                .asInstanceOf(InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        assertThat(result)
                .extracting(Output::getValue)
                .isNotNull();
        assertThat(result).extracting(Output::isError)
                .asInstanceOf(InstanceOfAssertFactories.BOOLEAN)
                .isFalse();
        assertThat(result)
                .extracting(Output::getError)
                .isNull();
    }
}
