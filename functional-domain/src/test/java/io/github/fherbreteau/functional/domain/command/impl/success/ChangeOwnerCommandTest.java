package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Group;
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
class ChangeOwnerCommandTest {
    private ChangeOwnerCommand command;
    @Mock
    private FileRepository repository;
    private User newUser;
    private Group group;
    @Mock
    private User actor;

    @Captor
    private ArgumentCaptor<Item> itemCaptor;

    @BeforeEach
    public void setup() {
        group = Group.builder("group").build();
        Item item = File.builder()
                .withName("name")
                .withOwner(User.builder("user").build())
                .withGroup(group)
                .build();
        newUser = User.builder("newUser").build();
        command = new ChangeOwnerCommand(repository, item, newUser);
    }

    @Test
    void shouldEditOwnerWhenExecutingCommand() {
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
                .extracting(Item::getOwner)
                .isEqualTo(newUser);
        assertThat(itemCaptor.getValue())
                .extracting(Item::getGroup)
                .isEqualTo(group);
    }
}
