package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateFileCommandTest {
    private CreateFileCommand command;
    @Mock
    private FileRepository repository;
    @Mock
    private AccessChecker accessChecker;
    private Folder parent;
    private User actor;

    @Captor
    private ArgumentCaptor<File> itemCaptor;

    @BeforeEach
    public void setup() {
        parent = Folder.builder()
                .withName("parent")
                .build();
        actor = User.user("actor");
        command = new CreateFileCommand(repository, accessChecker, "file", parent);
    }

    @Test
    void shouldCheckWriteAccessToFolderWhenCheckingCommand() {
        // GIVEN
        given(accessChecker.canWrite(parent, actor)).willReturn(true);
        // WHEN
        boolean result = command.canExecute(actor);
        // THEN
        assertThat(result).isTrue();
    }

    @Test
    void shouldCreateFileWhenExecutingCommand() {
        // GIVEN
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        // WHEN
        Item<?, ?> result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull();
        verify(repository).save(itemCaptor.capture());
        assertThat(itemCaptor.getValue())
                .isInstanceOf(File.class)
                .extracting(Item::getParent)
                .isEqualTo(parent);
        assertThat(itemCaptor.getValue())
                .extracting(Item::getName)
                .isEqualTo("file");
        assertThat(itemCaptor.getValue())
                .extracting(Item::getOwner)
                .isEqualTo(actor);
        assertThat(itemCaptor.getValue())
                .extracting(Item::getGroup)
                .isEqualTo(actor.getGroup());
    }

    @Test
    void shouldGenerateAndErrorWhenExecutingErrorHandling() {
        // GIVEN
        // WHEN
        Error error = command.handleError(actor);
        //THEN
        assertThat(error).isNotNull()
                .extracting(Error::getMessage)
                .isEqualTo("TOUCH with arguments Input{item='parent null:null --------- null', name='file', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, content=<redacted>} failed for actor");
    }
}
