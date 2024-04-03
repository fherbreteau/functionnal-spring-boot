package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
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
public class ChangeModeCommandTest {
    private ChangeModeCommand command;
    @Mock
    private FileRepository repository;
    @Mock
    private AccessChecker accessChecker;
    private File item;
    private AccessRight ownerAccess;
    private AccessRight groupAccess;
    private AccessRight otherAccess;
    @Mock
    private User actor;

    @Captor
    private ArgumentCaptor<Item<?, ?>> itemCaptor;

    @BeforeEach
    public void setup() {
        item = File.builder()
                .withName("name")
                .withOwner(User.user("user"))
                .withGroup(Group.group("group"))
                .build();
        ownerAccess = AccessRight.full();
        groupAccess = AccessRight.full();
        otherAccess = AccessRight.full();
        command = new ChangeModeCommand(repository, accessChecker, item, ownerAccess, groupAccess, otherAccess);
    }

    @Test
    void shouldCheckChangeModeAccessToItemWhenCheckingCommand() {
        // GIVEN
        given(accessChecker.canChangeMode(item, actor)).willReturn(true);
        // WHEN
        boolean result = command.canExecute(actor);
        // THEN
        assertThat(result).isTrue();
    }

    @Test
    void shouldEditModeWhenExecutingCommand() {
        // GIVEN
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        // WHEN
        Item<?, ?> result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull();
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

    @Test
    void shouldGenerateAndErrorWhenExecutingErrorHandling() {
        // GIVEN
        // WHEN
        Error error = command.handleError(actor);
        //THEN
        assertThat(error).isNotNull()
                .extracting(Error::getMessage)
                .isEqualTo("CHMOD with arguments Input{item='name user:group --------- null', name='null', user=null, group=null, ownerAccess=rwx, groupAccess=rwx, otherAccess=rwx, content=<redacted>} failed for actor");
    }
}
