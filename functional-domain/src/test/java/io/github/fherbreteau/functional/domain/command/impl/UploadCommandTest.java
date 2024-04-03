package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
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
public class UploadCommandTest {
    private UploadCommand command;
    @Mock
    private FileRepository repository;
    @Mock
    private AccessChecker accessChecker;
    private File file;
    private User actor;

    @Captor
    private ArgumentCaptor<Item<?, ?>> itemCaptor;

    @BeforeEach
    public void setup() {
        file = File.builder()
                .withName("file")
                .build();
        actor = User.user("actor");
        command = new UploadCommand(repository, accessChecker, file, new byte[0]);
    }

    @Test
    void shouldCheckWriteAccessToFileWhenCheckingCommand() {
        // GIVEN
        given(accessChecker.canWrite(file, actor)).willReturn(true);
        // WHEN
        boolean result = command.canExecute(actor);
        // THEN
        assertThat(result).isTrue();
    }

    @Test
    void shouldWriteContentWhenExecutingCommand() {
        // GIVEN
        // WHEN
        command.execute(actor);
        //THEN
        verify(repository).writeContent(file, new byte[0]);
    }

    @Test
    void shouldGenerateAndErrorWhenExecutingErrorHandling() {
        // GIVEN
        // WHEN
        Error error = command.handleError(actor);
        //THEN
        assertThat(error).isNotNull()
                .extracting(Error::getMessage)
                .isEqualTo("UPLOAD with arguments Input{item='file null:null --------- null', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, content=<redacted>} failed for actor");
    }
}
