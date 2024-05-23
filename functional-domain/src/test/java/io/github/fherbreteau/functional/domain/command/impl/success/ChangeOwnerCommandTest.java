package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessUpdater;
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
class ChangeOwnerCommandTest {
    private ChangeOwnerCommand command;
    @Mock
    private FileRepository repository;
    @Mock
    private AccessUpdater accessUpdater;
    @Mock
    private User actor;

    private User oldUser;
    private User newUser;
    private Group group;

    @Captor
    private ArgumentCaptor<Item> itemCaptor;
    @Captor
    private ArgumentCaptor<User> ownerCaptor;

    @BeforeEach
    public void setup() {
        oldUser = User.builder("user").build();
        group = Group.builder("group").build();
        Item item = File.builder()
                .withName("name")
                .withOwner(oldUser)
                .withGroup(group)
                .build();
        newUser = User.builder("newUser").build();
        command = new ChangeOwnerCommand(repository, accessUpdater, item, newUser);
    }

    @Test
    void shouldEditOwnerWhenExecutingCommand() {
        // GIVEN
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(accessUpdater.updateOwner(any(), any())).willAnswer(invocation -> invocation.getArgument(0));
        // WHEN
        Output result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(repository).save(itemCaptor.capture());
        assertThat(itemCaptor.getValue())
                .extracting(Item::getOwner)
                .isEqualTo(newUser);
        assertThat(itemCaptor.getValue())
                .extracting(Item::getGroup)
                .isEqualTo(group);
        verify(accessUpdater).updateOwner(any(), ownerCaptor.capture());
        assertThat(ownerCaptor.getValue())
                .isEqualTo(oldUser);
    }
}
