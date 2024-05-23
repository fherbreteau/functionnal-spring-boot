package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class DeleteItemCommandTest {
    private DeleteItemCommand command;
    @Mock
    private FileRepository repository;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private AccessUpdater accessUpdater;

    private File file;
    private User actor;

    @BeforeEach
    public void setup() {
        file = File.builder().withName("file").withParent(Folder.getRoot()).build();
        actor = User.builder("actor").build();
        command = new DeleteItemCommand(repository, contentRepository, accessUpdater, file);
    }

    @Test
    void shouldDeleteTheGivenItem() {

        given(repository.delete(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(accessUpdater.deleteItem(any())).willAnswer(invocation -> invocation.getArgument(0));
        willDoNothing().given(contentRepository).deleteContent(file);

        Output output = command.execute(actor);

        assertThat(output).extracting(Output::getValue)
                .isEqualTo(file);
    }

}
