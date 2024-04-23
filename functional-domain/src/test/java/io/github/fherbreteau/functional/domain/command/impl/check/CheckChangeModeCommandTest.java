package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.command.impl.error.ErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeModeCommand;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckChangeModeCommandTest {
    private CheckChangeModeCommand command;
    @Mock
    private FileRepository repository;
    @Mock
    private AccessChecker accessChecker;
    private File item;
    @Mock
    private User actor;

    @BeforeEach
    public void setup() {
        item = File.builder()
                .withName("name")
                .withOwner(User.user("user"))
                .withGroup(Group.group("group"))
                .build();
        AccessRight ownerAccess = AccessRight.full();
        AccessRight groupAccess = AccessRight.full();
        AccessRight otherAccess = AccessRight.full();
        command = new CheckChangeModeCommand(repository, accessChecker, item, ownerAccess, groupAccess, otherAccess);
    }

    @Test
    void shouldGenerateChangeModeCommandWhenCheckingSucceed() {
        // GIVEN
        given(accessChecker.canChangeMode(item, actor)).willReturn(true);
        // WHEN
        Command<Output> result = command.execute(actor);
        // THEN
        assertThat(result).isInstanceOf(ChangeModeCommand.class);
    }

    @Test
    void shouldGenerateErrorCommandWhenCheckingFails() {
        // GIVEN
        given(accessChecker.canChangeMode(item, actor)).willReturn(false);
        // WHEN
        Command<Output> result = command.execute(actor);
        //THEN
        assertThat(result).isInstanceOf(ErrorCommand.class);
    }
}
