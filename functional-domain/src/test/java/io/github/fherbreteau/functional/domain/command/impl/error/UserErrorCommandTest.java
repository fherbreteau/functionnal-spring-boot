package io.github.fherbreteau.functional.domain.command.impl.error;

import io.github.fherbreteau.functional.domain.entities.*;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserErrorCommandTest {

    @Mock
    private User actor;

    public static Stream<Arguments> shouldGenerateAnErrorWhenExecutingCommand() {
        return Stream.of(UserCommandType.values())
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void shouldGenerateAnErrorWhenExecutingCommand(UserCommandType type) {
        // GIVEN
        UserErrorCommand command = new UserErrorCommand(type, UserInput.builder("error").build());
        // WHEN
        Output result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isError, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
    }
}
