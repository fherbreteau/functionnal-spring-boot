package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SuppressWarnings({"rawtypes", "unchecked"})
@ExtendWith(MockitoExtension.class)
class ChangeModeCommandTest {
    @Mock
    private FileRepository repository;
    @Mock
    private User actor;

    @Captor
    private ArgumentCaptor<Item> itemCaptor;

    public static Stream<Arguments> shouldEditModeWhenExecutingCommand() {
        return Stream.of(
                Arguments.of(AccessRight.full(), null, null),
                Arguments.of(null, AccessRight.full(), null),
                Arguments.of(null, null, AccessRight.full())
        );
    }

    @ParameterizedTest
    @MethodSource
    void shouldEditModeWhenExecutingCommand(AccessRight ownerAccess, AccessRight groupAccess, AccessRight otherAccess) {
        // GIVEN
        File item = File.builder()
                .withName("name")
                .withOwner(User.user("user"))
                .withGroup(Group.group("group"))
                .withOwnerAccess(null)
                .withGroupAccess(null)
                .withOtherAccess(null)
                .build();
        ChangeModeCommand command = new ChangeModeCommand(repository, item, ownerAccess, groupAccess, otherAccess);
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
