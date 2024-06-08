package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeOwnerCommand;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckChangeOwnerCommandTest {
    private CheckChangeOwnerCommand command;
    @Mock
    private ItemRepository repository;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private AccessUpdater accessUpdater;
    @Mock
    private User actor;

    private File item;

    @BeforeEach
    public void setup() {
        Group group = Group.builder("group").build();
        item = File.builder()
                .withName("name")
                .withOwner(User.builder("user").build())
                .withGroup(group)
                .build();
        User newUser = User.builder("newUser").build();
        command = new CheckChangeOwnerCommand(repository, accessChecker, accessUpdater, item, newUser);
    }

    @Test
    void shouldGenerateChangeOwnerCommandWhenCheckingSucceed() {
        // GIVEN
        given(accessChecker.canChangeOwner(item, actor)).willReturn(true);
        // WHEN
        Command<Output<Item>> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(ChangeOwnerCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(accessChecker.canChangeOwner(item, actor)).willReturn(false);
        // WHEN
        Command<Output<Item>> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(ItemErrorCommand.class);
    }
}
