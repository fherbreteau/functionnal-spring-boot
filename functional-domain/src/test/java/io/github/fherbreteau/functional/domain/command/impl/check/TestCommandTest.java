package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestCommandTest {

    @Test
    void shouldThrowAnExceptionWhenItemCheckFailed() {
        // GIVEN
        AbstractCheckItemCommand<Item, Command<Output<Item>>> command = new AbstractCheckItemCommand<>(null, null) {
            @Override
            protected List<String> checkAccess(User actor) {
                List<String> reasons = new ArrayList<>();
                if (isNull(actor)) {
                    reasons.add("Error");
                }
                return reasons;
            }

            @Override
            protected Command<Output<Item>> createSuccess() {
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
        AbstractCheckUserCommand<User, Command<Output<User>>> command = new AbstractCheckUserCommand<>(null, null, null, null) {
            @Override
            protected List<String> checkAccess(User actor) {
                List<String> reasons = new ArrayList<>();
                if (isNull(actor)) {
                    reasons.add("Error");
                }
                return reasons;
            }

            @Override
            protected Command<Output<User>> createSuccess() {
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
