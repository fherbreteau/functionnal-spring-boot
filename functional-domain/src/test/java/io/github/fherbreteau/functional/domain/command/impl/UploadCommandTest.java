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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DownloadCommandTest {
    private DownloadCommand command;
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
        command = new DownloadCommand(repository, accessChecker, file);
    }

    @Test
    void shouldCheckChangeGroupAccessToFileWhenCheckingCommand() {
        // GIVEN
        given(accessChecker.canRead(file, actor)).willReturn(true);
        // WHEN
        boolean result = command.canExecute(actor);
        // THEN
        assertThat(result).isTrue();
    }

    @Test
    void shouldEditGroupWhenExecutingCommand() {
        // GIVEN
        given(repository.readContent(file)).willReturn(new byte[0]);
        // WHEN
        byte[] result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void shouldGenerateAndErrorWhenExecutingErrorHandling() {
        // GIVEN
        // WHEN
        Error error = command.handleError(actor);
        //THEN
        assertThat(error).isNotNull()
                .extracting(Error::getMessage)
                .isEqualTo("DOWNLOAD with arguments Input{item='file null:null --------- null', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, content=<redacted>} failed for actor");
    }
}
