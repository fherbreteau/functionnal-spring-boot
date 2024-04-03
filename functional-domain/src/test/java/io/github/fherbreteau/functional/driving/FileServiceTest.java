package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.command.*;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.path.Path;
import io.github.fherbreteau.functional.domain.path.PathFactory;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    private FileService fileService;
    @Mock
    private CompositeFactory commandFactory;
    @Mock
    private PathFactory pathFactory;
    @Mock
    private User actor;
    @Mock
    private Command<Object> command;
    @Mock
    private Item<File, File.Builder> item;

    @BeforeEach
    public void setup() {
        fileService = new FileService(commandFactory, pathFactory);
    }

    @Test
    void testAccessRootPathShouldSucceed() {
        // GIVEN
        given(pathFactory.getRoot()).willReturn(Path.ROOT);
        // WHEN
        Path path = fileService.getPath("/", actor);
        // THEN
        assertThat(path).isNotNull()
                .satisfies(p -> assertThat(path.isError()).isFalse())
                .satisfies(p -> assertThat(path.getItem()).isEqualTo(Folder.getRoot()));
    }

    @Test
    void testAccessFolderAtFirstLevelByPathShouldSucceed() {
        // GIVEN
        given(pathFactory.getRoot()).willReturn(Path.ROOT);
        given(pathFactory.resolve(eq(Path.ROOT), anyString(), eq(actor))).willAnswer(invocation -> {
            Path parent = invocation.getArgument(0);
            String element = invocation.getArgument(1);
            return Path.success(element, createFolder(parent.getItemAsFolder(), element));
        });
        // WHEN
        Path path = fileService.getPath("/first", actor);
        // THEN
        assertThat(path).isNotNull()
                .satisfies(p -> assertThat(p.isError()).isFalse())
                .satisfies(p -> assertThat(path.getName()).isEqualTo("first"))
                .satisfies(p -> assertThat(path.getItem()).isInstanceOf(Folder.class));
    }

    @Test
    void testAccessFileAtFirstLevelByPathShouldSucceed() {
        // GIVEN
        given(pathFactory.getRoot()).willReturn(Path.ROOT);
        given(pathFactory.resolve(eq(Path.ROOT), anyString(), eq(actor))).willAnswer(invocation -> {
            Path parent = invocation.getArgument(0);
            String element = invocation.getArgument(1);
            return Path.success(element, createFile(parent.getItemAsFolder(), element));
        });
        // WHEN
        Path path = fileService.getPath("/first", actor);
        // THEN
        assertThat(path).isNotNull()
                .satisfies(p -> assertThat(p.isError()).isFalse())
                .satisfies(p -> assertThat(path.getName()).isEqualTo("first"))
                .satisfies(p -> assertThat(path.getItem()).isInstanceOf(File.class));
    }

    @Test
    void testAccessFileAtSecondLevelByPathShouldSucceed() {
        // GIVEN
        given(pathFactory.getRoot()).willReturn(Path.ROOT);
        given(pathFactory.resolve(eq(Path.ROOT), anyString(), eq(actor))).willAnswer(invocation -> {
            Path parent = invocation.getArgument(0);
            String element = invocation.getArgument(1);
            return Path.success(element, createFolder(parent.getItemAsFolder(), element));
        });
        given(pathFactory.resolve(argThat(arg -> arg.getItem() != Path.ROOT.getItem()), anyString(), eq(actor))).willAnswer(invocation -> {
            Path parent = invocation.getArgument(0);
            String element = invocation.getArgument(1);
            return Path.success(element, createFile(parent.getItemAsFolder(), element));
        });
        // WHEN
        Path path = fileService.getPath("/first/second", actor);
        // THEN
        assertThat(path).isNotNull()
                .satisfies(p -> assertThat(p.isError()).isFalse())
                .satisfies(p -> assertThat(path.getName()).isEqualTo("second"))
                .satisfies(p -> assertThat(path.getItem()).isInstanceOf(File.class));
    }

    @Test
    void testAccessFileAtSecondLevelByPathShouldFailWhenFirstLevelIsAnError() {
        // GIVEN
        given(pathFactory.getRoot()).willReturn(Path.ROOT);
        given(pathFactory.resolve(eq(Path.ROOT), anyString(), eq(actor))).willAnswer(invocation -> {
            Path parent = invocation.getArgument(0);
            String element = invocation.getArgument(1);
            User actor = invocation.getArgument(2);
            return Path.error(new Error(createFile(parent.getItemAsFolder(), element), actor));
        });
        // WHEN
        Path path = fileService.getPath("/first/second", actor);
        // THEN
        assertThat(path).isNotNull()
                .satisfies(p -> assertThat(p.isError()).isTrue())
                .satisfies(p -> assertThat(p.getError().getMessage()).isEqualTo("'first null:null --------- ' is not executable for actor"));
    }

    @Test
    void testAccessFileAtSecondLevelByPathShouldFailWhenFirstLevelIsAFile() {
        // GIVEN
        given(pathFactory.getRoot()).willReturn(Path.ROOT);
        given(pathFactory.resolve(eq(Path.ROOT), anyString(), eq(actor))).willAnswer(invocation -> {
            Path parent = invocation.getArgument(0);
            String element = invocation.getArgument(1);
            return Path.success(element, createFile(parent.getItemAsFolder(), element));
        });
        // WHEN
        Path path = fileService.getPath("/first/second", actor);
        // THEN
        assertThat(path).isNotNull()
                .satisfies(p -> assertThat(p.isError()).isTrue())
                .satisfies(p -> assertThat(p.getError().getMessage()).isEqualTo("'first null:null --------- ' is not a folder"));
    }

    @Test
    void testProcessKnownCommandShouldOutputAResult() {
        // Given
        given(commandFactory.createCommand(any(), any())).willReturn(command);
        given(command.canExecute(actor)).willReturn(true);
        given(command.execute(actor)).willReturn(new Object());
        // When
        Output result = fileService.processCommand(CommandType.TOUCH, actor, Input.builder(item).build());
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
        given(commandFactory.createCommand(any(), any())).willReturn(command);
        given(command.canExecute(actor)).willReturn(false);
        given(command.handleError(actor)).willReturn(new Error("message"));
        // When
        Output result = fileService.processCommand(CommandType.TOUCH, actor, Input.builder(item).build());
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
                .isNotNull();
    }

    private Item<?, ?> createFolder(Folder parent, String element) {
        return Folder.builder().withName(element).withParent(parent).build();
    }

    private Item<?, ?> createFile(Folder parent, String element) {
        return File.builder().withName(element).withParent(parent).build();
    }

}
