package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.DeleteItemCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckDeleteItemCommandTest {
    private CheckDeleteItemCommand command;
    @Mock
    private ItemRepository repository;
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
        Item item = File.builder()
                .withName("to_delete")
                .withParent(parent)
                .build();
        actor = User.builder("actor").build();
        command = new CheckDeleteItemCommand(repository, contentRepository, accessChecker, accessUpdater, item);
    }

    @Test
    void shouldGenerateDeleteItemCommandWhenCheckingSucceed() {
        // GIVEN
        given(accessChecker.canWrite(parent, actor)).willReturn(true);
        // WHEN
        Command<Output<Void>> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(DeleteItemCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(accessChecker.canWrite(parent, actor)).willReturn(false);
        // WHEN
        Command<Output<Void>> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(ItemErrorCommand.class)
                .extracting("reasons", list(String.class))
                .hasSize(1)
                .first().matches(s -> s.endsWith(" can't delete 'to_delete null:null --------- parent'"));
    }
}
