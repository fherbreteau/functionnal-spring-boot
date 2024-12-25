package io.github.fherbreteau.functional.domain.command.impl.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import io.github.fherbreteau.functional.domain.entities.Failure;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemFailureCommandTest {

    @Mock
    private User actor;

    public static Stream<Arguments> shouldGenerateAnFailureWhenExecutingCommand() {
        return Stream.of(ItemCommandType.values())
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void shouldGenerateAnFailureWhenExecutingCommand(ItemCommandType itemCommandType) {
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
                .extracting(Output::isFailure, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        assertThat(result).extracting(Output::getFailure, type(Failure.class))
                .extracting(Failure::getReasons, list(String.class))
                .containsExactly("error1", "error2");
    }
}
