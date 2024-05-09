package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommandFactoriesTest {

    @Mock
    private FileRepository repository;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private User actor;

    @Test
    void shouldCreateAFileWithTheGivenName() {
        CommandFactory factory = new CreateItemCommandFactory();
        Folder folder = Folder.builder().withName("folder").withParent(Folder.getRoot()).build();
        Input input = Input.builder(folder).withName("file").build();

        given(accessChecker.canWrite(folder, actor)).willReturn(true);
        given(repository.exists(folder, "file")).willReturn(false);
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        Command<Command<Output>> checker = factory.createCommand(repository, accessChecker, CommandType.TOUCH, input);
        Command<Output> executor = checker.execute(actor);
        Output output = executor.execute(actor);

        assertThat(output).extracting(Output::getValue, type(File.class))
                .isNotNull()
                .extracting(AbstractItem::getPath)
                .isEqualTo("/folder/file");
    }

    @Test
    void shouldChangeTheAccessOfTheFileWithTheGivenAccessRight() {
        CommandFactory factory = new ChangeModeCommandFactory();
        File file = File.builder().withName("file").withParent(Folder.getRoot()).build();
        Input input = Input.builder(file)
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readExecute())
                .withOtherAccess(AccessRight.executeOnly())
                .build();

        given(accessChecker.canChangeMode(file, actor)).willReturn(true);
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        Command<Command<Output>> checker = factory.createCommand(repository, accessChecker, CommandType.CHMOD, input);
        Command<Output> executor = checker.execute(actor);
        Output output = executor.execute(actor);

        assertThat(output).extracting(Output::getValue, type(File.class))
                .isNotNull()
                .satisfies(i -> {
                    assertThat(i.getOwnerAccess()).isEqualTo(AccessRight.full());
                    assertThat(i.getGroupAccess()).isEqualTo(AccessRight.readExecute());
                    assertThat(i.getOtherAccess()).isEqualTo(AccessRight.executeOnly());
                });
    }

    @Test
    void testInputHasRequiredInfoInToString() {
        File file = File.builder().withName("file").withParent(Folder.getRoot()).build();
        Input input = Input.builder(file).build();
        assertThat(input).hasToString("Input{item='file null:null --------- ', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, content=<redacted>}");
    }
}
