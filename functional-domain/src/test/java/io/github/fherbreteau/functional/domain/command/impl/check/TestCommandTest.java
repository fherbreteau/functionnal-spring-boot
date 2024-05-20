package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestCommandTest {

    @Test
    void shouldThrowAnExceptionWhenItemCheckFailed() {
        // GIVEN
        AbstractCheckItemCommand<Command<Output>> command = new AbstractCheckItemCommand<>(null, null) {
            @Override
            protected boolean checkAccess(User actor) {
                return actor != null;
            }

            @Override
            protected Command<Output> createSuccess() {
                throw new UnsupportedOperationException("Should never be called");
            }
        };
        // WHEN
        assertThatThrownBy(() -> command.execute(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Unsupported Command always succeed");
        User actor = User.builder("actor").build();
        assertThatThrownBy(() -> command.execute(actor))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Should never be called");
    }

    @Test
    void shouldThrowAnExceptionWhenUserCheckFailed() {
        // GIVEN
        AbstractCheckUserCommand<Command<Output>> command = new AbstractCheckUserCommand<>(null, null, null) {
            @Override
            protected boolean checkAccess(User actor) {
                return actor != null;
            }

            @Override
            protected Command<Output> createSuccess() {
                throw new UnsupportedOperationException("Should never be called");
            }
        };
        // WHEN
        assertThatThrownBy(() -> command.execute(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Unsupported Command always succeed");
        User actor = User.builder("actor").build();
        assertThatThrownBy(() -> command.execute(actor))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Should never be called");
    }
}
