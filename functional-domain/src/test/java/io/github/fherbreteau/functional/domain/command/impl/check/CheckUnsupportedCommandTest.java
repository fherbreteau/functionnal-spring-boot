package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.command.impl.error.ErrorCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CheckUnsupportedCommandTest {
    private CheckUnsupportedCommand command;
    private User actor;

    @BeforeEach
    public void setup() {
        actor = User.user("actor");
        command = new CheckUnsupportedCommand(null, null, null, null);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingSucceed() {
        // GIVEN
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(ErrorCommand.class);
    }
}
