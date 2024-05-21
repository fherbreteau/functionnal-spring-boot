package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.command.*;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckUnsupportedItemCommand;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.path.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.PathParser;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    private FileService fileService;
    @Mock
    private CompositeItemCommandFactory commandFactory;
    @Mock
    private CompositePathFactory pathFactory;
    @Mock
    private User actor;
    @Mock
    private CheckCommand<Output> checkCommand;
    @Mock
    private Command<Output> executeCommand;
    @Mock
    private Item item;

    @BeforeEach
    public void setup() {
        fileService = new FileService(commandFactory, pathFactory);
    }

    @Test
    void testAccessExistingPathShouldSucceed() {
        // GIVEN
        PathParser parser = mock(PathParser.class);
        given(parser.resolve(actor)).willReturn(Path.ROOT);
        given(pathFactory.createParser(Path.ROOT, "/")).willReturn(parser);
        // WHEN
        Path path = fileService.getPath("/", actor);
        // THEN
        assertThat(path).isNotNull()
                .satisfies(p -> assertThat(path.isError()).isFalse())
                .satisfies(p -> assertThat(path.getItem()).isEqualTo(Folder.getRoot()));
    }

    @Test
    void testAccessUnknownPathShouldFail() {
        // GIVEN
        Path error = Path.error(Error.error(String.format("%s not found in %s for %s", "unknown", Folder.getRoot(), actor)));
        PathParser parser = mock(PathParser.class);
        given(parser.resolve(actor)).willReturn(error);
        given(pathFactory.createParser(Path.ROOT, "/unknown")).willReturn(parser);

        // WHEN
        Path path = fileService.getPath("/unknown", actor);
        // THEN
        assertThat(path).isNotNull()
                .satisfies(p -> assertThat(p.isError()).isTrue())
                .satisfies(p -> assertThat(p.getError().getMessage()).isEqualTo("unknown not found in ' null:null ------rwx null' for actor"))
                .satisfies(p -> assertThat(p.getError().getReasons()).isEmpty());
    }

    @Test
    void testProcessKnownCommandShouldOutputAResult() {
        // Given
        given(commandFactory.createCommand(any(), any())).willReturn(checkCommand);
        given(checkCommand.execute(actor)).willReturn(executeCommand);
        given(executeCommand.execute(actor)).willReturn(new Output(new Object()));
        // When
        Output result = fileService.processCommand(ItemCommandType.TOUCH, actor, ItemInput.builder(item).build());
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

    @Test
    void testProcessUnknownCommandShouldOutputAResult() {
        // Given
        given(commandFactory.createCommand(any(), any()))
                .willAnswer(invocation -> {
                    ItemCommandType type = invocation.getArgument(0);
                    ItemInput itemInput = invocation.getArgument(1);
                    return new CheckUnsupportedItemCommand(null, null, type, itemInput);
                });
        // When
        Output result = fileService.processCommand(ItemCommandType.TOUCH, actor, ItemInput.builder(item).build());
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess)
                .asInstanceOf(InstanceOfAssertFactories.BOOLEAN)
                .isFalse();
        assertThat(result)
                .extracting(Output::getValue)
                .isNull();
        assertThat(result).extracting(Output::isError)
                .asInstanceOf(InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        assertThat(result)
                .extracting(Output::getError)
                .isNotNull()
                .extracting(Error::getMessage)
                .isNotNull();
        assertThat(result)
                .extracting(Output::getError)
                .extracting(Error::getReasons, list(String.class))
                .isEmpty();
    }
}
