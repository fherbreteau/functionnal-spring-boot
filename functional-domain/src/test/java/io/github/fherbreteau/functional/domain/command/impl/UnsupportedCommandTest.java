package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.exception.UnsupportedCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UnsupportedCommandTest {
    private UnsupportedCommand command;
    private User actor;

    @BeforeEach
    public void setup() {
        actor = User.user("actor");
        command = new UnsupportedCommand(null, null);
    }

    @Test
    void shouldReturnFalseWhenCheckingCommand() {
        // GIVEN
        // WHEN
        boolean result = command.canExecute(actor);
        // THEN
        assertThat(result).isFalse();
    }

    @Test
    void shouldEditGroupWhenExecutingCommand() {
        // GIVEN
        // WHEN
        assertThatThrownBy(() -> command.execute(actor))
                //THEN
                .isInstanceOf(UnsupportedCommandException.class);
    }

    @Test
    void shouldGenerateAndErrorWhenExecutingErrorHandling() {
        // GIVEN
        // WHEN
        Error error = command.handleError(actor);
        //THEN
        assertThat(error).isNotNull()
                .extracting(Error::getMessage)
                .isEqualTo("null with arguments null failed for actor");
    }
}
