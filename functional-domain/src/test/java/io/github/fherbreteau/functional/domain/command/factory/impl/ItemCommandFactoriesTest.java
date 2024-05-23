package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
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
class ItemCommandFactoriesTest {

    @Mock
    private FileRepository repository;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private AccessUpdater accessUpdater;
    @Mock
    private User actor;

    @Test
    void shouldCreateAFileWithTheGivenName() {
        ItemCommandFactory factory = new CreateItemCommandFactory();
        Folder folder = Folder.builder().withName("folder").withParent(Folder.getRoot()).build();
        ItemInput itemInput = ItemInput.builder(folder).withName("file").build();

        given(accessChecker.canWrite(folder, actor)).willReturn(true);
        given(repository.exists(folder, "file")).willReturn(false);
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(accessUpdater.createItem(any())).willAnswer(invocation -> invocation.getArgument(0));

        Command<Command<Output>> checker = factory.createCommand(repository, contentRepository, accessChecker,
                accessUpdater, ItemCommandType.TOUCH, itemInput);
        Command<Output> executor = checker.execute(actor);
        Output output = executor.execute(actor);

        assertThat(output).extracting(Output::getValue, type(File.class))
                .isNotNull()
                .extracting(AbstractItem::getPath)
                .isEqualTo("/folder/file");
    }

    @Test
    void shouldChangeTheAccessOfTheFileWithTheGivenAccessRight() {
        ItemCommandFactory factory = new ChangeModeCommandFactory();
        File file = File.builder().withName("file").withParent(Folder.getRoot()).build();
        ItemInput itemInput = ItemInput.builder(file)
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readExecute())
                .withOtherAccess(AccessRight.executeOnly())
                .build();

        given(accessChecker.canChangeMode(file, actor)).willReturn(true);
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        Command<Command<Output>> checker = factory.createCommand(repository, contentRepository, accessChecker,
                accessUpdater, ItemCommandType.CHMOD, itemInput);
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
    void shouldUpdateTheContentTypeOfTheFileWithTheGivenContentType() {
        ItemCommandFactory factory = new UploadCommandFactory();
        File file = File.builder().withName("file").withParent(Folder.getRoot()).withContentType("oldValue").build();
        ItemInput itemInput = ItemInput.builder(file)
                .withContentType("newValue")
                .build();

        given(accessChecker.canWrite(file, actor)).willReturn(true);
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        Command<Command<Output>> checker = factory.createCommand(repository, contentRepository, accessChecker,
                accessUpdater, ItemCommandType.CHMOD, itemInput);
        Command<Output> executor = checker.execute(actor);
        Output output = executor.execute(actor);

        assertThat(output).extracting(Output::getValue, type(File.class))
                .isNotNull()
                .extracting(File::getContentType)
                .isEqualTo("newValue");
    }

    @Test
    void shouldDeleteTheGivenItem() {
        ItemCommandFactory factory = new DeleteItemCommandFactory();
        Folder folder = Folder.builder().withName("folder").withParent(Folder.getRoot()).build();
        ItemInput itemInput = ItemInput.builder(folder).build();

        given(accessChecker.canWrite(Folder.getRoot(), actor)).willReturn(true);
        given(repository.delete(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(accessUpdater.deleteItem(any())).willAnswer(invocation -> invocation.getArgument(0));

        Command<Command<Output>> checker = factory.createCommand(repository, contentRepository, accessChecker,
                accessUpdater, ItemCommandType.DELETE, itemInput);
        Command<Output> executor = checker.execute(actor);
        Output output = executor.execute(actor);

        assertThat(output).extracting(Output::getValue, type(Folder.class))
                .isEqualTo(folder);
    }

    @Test
    void testInputHasRequiredInfoInToString() {
        File file = File.builder().withName("file").withParent(Folder.getRoot()).build();
        ItemInput itemInput = ItemInput.builder(file).build();
        assertThat(itemInput).hasToString("Input{item='file null:null --------- ', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null}");
    }

    @Test
    void testOutputHasRequiredInfoInToString() {
        Output output = new Output("success");
        assertThat(output).hasToString("Output{value=success}");
        output = new Output(Error.error("error"));
        assertThat(output).hasToString("Output{error=Error{message='error', reasons=[]}}");
    }
}
