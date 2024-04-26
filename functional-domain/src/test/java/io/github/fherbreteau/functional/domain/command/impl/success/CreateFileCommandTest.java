package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
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
        command = new CreateFileCommand(repository, "file", parent);
    }

    @Test
    void shouldCreateFileWhenExecutingCommand() {
        // GIVEN
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        // WHEN
        Output result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
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
}
