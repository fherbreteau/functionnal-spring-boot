package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.entities.*;
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

@SuppressWarnings({"rawtypes", "unchecked"})
@ExtendWith(MockitoExtension.class)
class ChangeModeCommandTest {
    private ChangeModeCommand command;
    @Mock
    private FileRepository repository;
    private AccessRight ownerAccess;
    private AccessRight groupAccess;
    private AccessRight otherAccess;
    @Mock
    private User actor;

    @Captor
    private ArgumentCaptor<Item> itemCaptor;

    @BeforeEach
    public void setup() {
        File item = File.builder()
                .withName("name")
                .withOwner(User.user("user"))
                .withGroup(Group.group("group"))
                .build();
        ownerAccess = AccessRight.full();
        groupAccess = AccessRight.full();
        otherAccess = AccessRight.full();
        command = new ChangeModeCommand(repository, item, ownerAccess, groupAccess, otherAccess);
    }

    @Test
    void shouldEditModeWhenExecutingCommand() {
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
                .extracting(Item::getOwnerAccess)
                .isEqualTo(ownerAccess);
        assertThat(itemCaptor.getValue())
                .extracting(Item::getGroupAccess)
                .isEqualTo(groupAccess);
        assertThat(itemCaptor.getValue())
                .extracting(Item::getOtherAccess)
                .isEqualTo(otherAccess);
    }
}
