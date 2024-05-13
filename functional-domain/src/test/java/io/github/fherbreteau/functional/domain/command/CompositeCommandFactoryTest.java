package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.factory.impl.*;
import io.github.fherbreteau.functional.domain.command.impl.check.*;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CompositeCommandFactoryTest {

    private CompositeCommandFactory factory;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private FileRepository repository;
    @Mock
    private ContentRepository contentRepository;

    public static Stream<Arguments> validCommandArguments() {
        Folder folder = mock(Folder.class);
        File file = mock(File.class);
        User user = mock(User.class);
        Group group = mock(Group.class);
        InputStream stream = mock(InputStream.class);
        return Stream.of(
                Arguments.of(CommandType.TOUCH, Input.builder(folder).withName("item").build(), CheckCreateFileCommand.class),
                Arguments.of(CommandType.MKDIR, Input.builder(folder).withName("item").build(), CheckCreateFolderCommand.class),
                Arguments.of(CommandType.LIST, Input.builder(folder).build(), CheckListChildrenCommand.class),
                Arguments.of(CommandType.CHOWN, Input.builder(file).withUser(user).build(), CheckChangeOwnerCommand.class),
                Arguments.of(CommandType.CHGRP, Input.builder(file).withGroup(group).build(), CheckChangeGroupCommand.class),
                Arguments.of(CommandType.CHMOD, Input.builder(file).withOwnerAccess(AccessRight.full()).build(), CheckChangeModeCommand.class),
                Arguments.of(CommandType.DOWNLOAD, Input.builder(file).build(), CheckDownloadCommand.class),
                Arguments.of(CommandType.UPLOAD, Input.builder(file).withContent(stream).withContentType("content").build(), CheckUploadCommand.class)
        );
    }

    public static Stream<Arguments> invalidCommandArguments() {
        Folder folder = mock(Folder.class);
        File file = mock(File.class);
        InputStream stream = mock(InputStream.class);
        return Stream.of(
                Arguments.of(CommandType.TOUCH, Input.builder(file).withName("item").build()),
                Arguments.of(CommandType.MKDIR, Input.builder(file).withName("item").build()),
                Arguments.of(CommandType.LIST, Input.builder(file).build()),
                Arguments.of(CommandType.CHOWN, Input.builder(file).build()),
                Arguments.of(CommandType.CHGRP, Input.builder(file).build()),
                Arguments.of(CommandType.CHMOD, Input.builder(file).build()),
                Arguments.of(CommandType.DOWNLOAD, Input.builder(folder).build()),
                Arguments.of(CommandType.UPLOAD, Input.builder(folder).withContent(stream).build())
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
        factory = new CompositeCommandFactory(repository, accessChecker, contentRepository, factories);
    }

    @ParameterizedTest(name = "Command of {0} with args {1} is supported")
    @MethodSource("validCommandArguments")
    void testCommandCreatedForSpecificTypeAndValidInput(CommandType type, Input input, Class<? extends Command<Command<Output>>> expected) {
        Command<?> command = factory.createCommand(type, input);

        assertThat(command).isNotNull().isInstanceOf(expected);
    }

    @ParameterizedTest(name = "Command of {0} with args {1} is not supported")
    @MethodSource("invalidCommandArguments")
    void testCommandCreatedForSpecificTypeAndInvalidInput(CommandType type, Input input) {
        Command<?> command = factory.createCommand(type, input);

        assertThat(command).isNotNull().isInstanceOf(CheckUnsupportedCommand.class);
    }
}
