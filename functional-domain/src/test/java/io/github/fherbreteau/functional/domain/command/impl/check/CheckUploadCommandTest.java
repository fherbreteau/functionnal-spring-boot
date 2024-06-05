package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.UploadCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckUploadCommandTest {
    private CheckUploadCommand command;
    @Mock
    private ItemRepository repository;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private InputStream inputStream;
    private File file;
    private User actor;

    @BeforeEach
    public void setup() {
        file = File.builder()
                .withName("file")
                .build();
        actor = User.builder("actor").build();
        command = new CheckUploadCommand(repository, accessChecker, contentRepository, file, inputStream, "contentType");
    }

    @Test
    void shouldGenerateUploadCommandWhenCheckingSucceed() {
        // GIVEN
        given(accessChecker.canWrite(file, actor)).willReturn(true);
        // WHEN
        Command<Output<Item>> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(UploadCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(accessChecker.canWrite(file, actor)).willReturn(false);
        // WHEN
        Command<Output<Item>> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(ItemErrorCommand.class);
    }
}
