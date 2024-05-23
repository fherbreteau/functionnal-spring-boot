package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeGroupCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckChangeGroupCommandTest {
    private CheckChangeGroupCommand command;
    @Mock
    private FileRepository repository;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private AccessUpdater accessUpdater;
    @Mock
    private User actor;

    private File item;

    @BeforeEach
    public void setup() {
        item = File.builder().withName("name").withOwner(User.builder("user").build()).build();
        Group group = Group.builder("group").build();
        command = new CheckChangeGroupCommand(repository, accessChecker, accessUpdater, item, group);
    }

    @Test
    void shouldGenerateChangeGroupCommandWhenCheckingSucceed() {
        // GIVEN
        given(accessChecker.canChangeGroup(item, actor)).willReturn(true);
        // WHEN
        Command<Output> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(ChangeGroupCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(accessChecker.canChangeGroup(item, actor)).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(ItemErrorCommand.class);
    }
}
