package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.CreateFileCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckCreateFileCommandTest {
    private CheckCreateFileCommand command;
    @Mock
    private FileRepository repository;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private AccessUpdater accessUpdater;

    private Folder parent;
    private User actor;

    @BeforeEach
    public void setup() {
        parent = Folder.builder()
                .withName("parent")
                .build();
        actor = User.builder("actor").build();
        command = new CheckCreateFileCommand(repository, contentRepository, accessChecker, accessUpdater, "file", parent);
    }

    @Test
    void shouldGenerateCreateFileCommandWhenCheckingSucceed() {
        // GIVEN
        given(accessChecker.canWrite(parent, actor)).willReturn(true);
        // WHEN
        Command<Output<Item>> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(CreateFileCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(accessChecker.canWrite(parent, actor)).willReturn(false);
        // WHEN
        Command<Output<Item>> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(ItemErrorCommand.class)
                .extracting("reasons", list(String.class))
                .hasSize(1)
                .first().matches(s -> s.endsWith(" can't create file in 'parent null:null --------- null'"));
    }

    @Test
    void shouldGenerateErrorCommandWhenExistenceCheckingFails() {
        // GIVEN
        given(accessChecker.canWrite(parent, actor)).willReturn(true);
        given(repository.exists(parent, "file")).willReturn(true);
        // WHEN
        Command<Output<Item>> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(ItemErrorCommand.class)
                .extracting("reasons", list(String.class))
                .hasSize(1)
                .first().isEqualTo("file already exists in  'parent null:null --------- null'");
        assertThat(result).extracting("type")
                .isEqualTo(ItemCommandType.TOUCH);
    }
}
