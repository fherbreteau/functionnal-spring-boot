package io.github.fherbreteau.functional.domain.command.impl.error;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserFailureCommandTest {

    @Mock
    private User actor;

    public static Stream<Arguments> shouldGenerateAnFailureWhenExecutingCommand() {
        return Stream.of(UserCommandType.values())
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void shouldGenerateAnFailureWhenExecutingCommand(UserCommandType type) {
        // GIVEN
        UserErrorCommand<Void> command = new UserErrorCommand<>(type, UserInput.builder("error").build());
        // WHEN
        Output<Void> result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isFailure, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
    }
}
