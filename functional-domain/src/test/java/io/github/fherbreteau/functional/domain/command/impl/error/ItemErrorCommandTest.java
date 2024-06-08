package io.github.fherbreteau.functional.domain.command.impl.error;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.entities.Error;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

@ExtendWith(MockitoExtension.class)
class ItemErrorCommandTest {

    @Mock
    private User actor;

    public static Stream<Arguments> shouldGenerateAnErrorWhenExecutingCommand() {
        return Stream.of(ItemCommandType.values())
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void shouldGenerateAnErrorWhenExecutingCommand(ItemCommandType itemCommandType) {
        // GIVEN
        Item item = File.builder()
                .withOwner(User.root())
                .withGroup(Group.root())
                .withHandle(UUID.randomUUID())
                .withName("")
                .build();
        List<String> reasons = List.of("error1", "error2");
        ItemErrorCommand<Void> command = new ItemErrorCommand<>(itemCommandType, ItemInput.builder(item).build(), reasons);
        // WHEN
        Output<Void> result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isError, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        assertThat(result).extracting(Output::getError, type(Error.class))
                .extracting(Error::getReasons, list(String.class))
                .containsExactly("error1", "error2");
    }
}
