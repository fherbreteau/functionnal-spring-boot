package io.github.fherbreteau.functional.domain.command.impl.error;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
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
        Item item = File.builder().build();
        ItemErrorCommand command = new ItemErrorCommand(itemCommandType, ItemInput.builder(item).build());
        // WHEN
        Output result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isError, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
    }
}
