package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class DeleteItemCommandTest {
    private DeleteItemCommand command;
    @Mock
    private ItemRepository repository;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private AccessUpdater accessUpdater;

    private File file;
    private User actor;

    @BeforeEach
    public void setup() {
        file = File.builder()
                .withName("file")
                .withOwner(User.root())
                .withGroup(Group.root())
                .withParent(Folder.getRoot())
                .build();
        actor = User.builder("actor").build();
        command = new DeleteItemCommand(repository, contentRepository, accessUpdater, file);
    }

    @Test
    void shouldDeleteTheGivenItem() {

        willDoNothing().given(repository).delete(any());
        willDoNothing().given(accessUpdater).deleteItem(any());
        given(contentRepository.deleteContent(file)).willReturn(Output.success(null));

        Output<Void> output = command.execute(actor);

        assertThat(output).extracting(Output::isSuccess, BOOLEAN)
                .isTrue();
    }

}
