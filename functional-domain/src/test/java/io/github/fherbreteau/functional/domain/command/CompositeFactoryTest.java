package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.factory.impl.*;
import io.github.fherbreteau.functional.domain.command.impl.UnsupportedCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CompositeFactoryTest {

    private CompositeFactory factory;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private FileRepository repository;

    public static Stream<Arguments> validCommandArguments() {
        Folder folder = mock(Folder.class);
        File file = mock(File.class);
        User user = mock(User.class);
        Group group = mock(Group.class);
        return Stream.of(
                Arguments.of(CommandType.TOUCH, new Input(folder, "item")),
                Arguments.of(CommandType.MKDIR, new Input(folder, "item")),
                Arguments.of(CommandType.LIST, new Input(folder)),
                Arguments.of(CommandType.CHOWN, new Input(file, user)),
                Arguments.of(CommandType.CHGRP, new Input(file, group)),
                Arguments.of(CommandType.CHMOD, new Input(file, AccessRight.full(), null, null)),
                Arguments.of(CommandType.DOWNLOAD, new Input(file)),
                Arguments.of(CommandType.UPLOAD, new Input(file, new byte[0]))
        );
    }

    public static Stream<Arguments> invalidCommandArguments() {
        Folder folder = mock(Folder.class);
        File file = mock(File.class);
        return Stream.of(
                Arguments.of(CommandType.TOUCH, new Input(file, "item")),
                Arguments.of(CommandType.MKDIR, new Input(file, "item")),
                Arguments.of(CommandType.LIST, new Input(file)),
                Arguments.of(CommandType.CHOWN, new Input(file)),
                Arguments.of(CommandType.CHGRP, new Input(file)),
                Arguments.of(CommandType.CHMOD, new Input(file)),
                Arguments.of(CommandType.DOWNLOAD, new Input(folder)),
                Arguments.of(CommandType.UPLOAD, new Input(folder, new byte[0]))
        );
    }

    @BeforeEach
    public void setup() {
        List<CommandFactory> factories = List.of(
                new ChangeOwnerCommandFactory(),
                new ChangeGroupCommandFactory(),
                new ChangeModeCommandFactory(),
                new CreateItemCommandFactory(),
                new DownloadCommandFactory(),
                new ListChildrenCommandFactory(),
                new UploadCommandFactory(),
                new UnsupportedCommandFactory());
        factory = new CompositeFactory(repository, accessChecker, factories);
    }

    @ParameterizedTest(name = "Command of {0} with args {1} is supported")
    @MethodSource("validCommandArguments")
    void testCommandCreatedForSpecificTypeAndValidInput(CommandType type, Input input) {
        Command<?> command = factory.createCommand(type, input);

        assertThat(command).isNotNull().isNotInstanceOf(UnsupportedCommand.class);
    }

    @ParameterizedTest(name = "Command of {0} with args {1} is not supported")
    @MethodSource("invalidCommandArguments")
    void testCommandCreatedForSpecificTypeAndInvalidInput(CommandType type, Input input) {
        Command<?> command = factory.createCommand(type, input);

        assertThat(command).isNotNull().isInstanceOf(UnsupportedCommand.class);
    }
}
