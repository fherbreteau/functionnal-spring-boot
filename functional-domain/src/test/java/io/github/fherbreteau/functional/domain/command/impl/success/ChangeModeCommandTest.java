package io.github.fherbreteau.functional.domain.command.impl.success;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChangeModeCommandTest {
    @Mock
    private ItemRepository repository;
    @Mock
    private AccessUpdater accessUpdater;
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
                .withOwner(User.builder("user").build())
                .withGroup(Group.builder("group").build())
                .withOwnerAccess(AccessRight.none())
                .withGroupAccess(AccessRight.none())
                .withOtherAccess(AccessRight.none())
                .build();
        ChangeModeCommand command = new ChangeModeCommand(repository, accessUpdater, item, ownerAccess, groupAccess,
                otherAccess);
        given(repository.update(any())).willAnswer(invocation -> invocation.getArgument(0));
        if (nonNull(ownerAccess)) {
            given(accessUpdater.updateOwnerAccess(any(), eq(AccessRight.none()))).willAnswer(invocation -> invocation.getArgument(0));
        }
        if (nonNull(groupAccess)) {
            given(accessUpdater.updateGroupAccess(any(), eq(AccessRight.none()))).willAnswer(invocation -> invocation.getArgument(0));
        }
        if (nonNull(otherAccess)) {
            given(accessUpdater.updateOtherAccess(any(), eq(AccessRight.none()))).willAnswer(invocation -> invocation.getArgument(0));
        }
        // WHEN
        Output<Item> result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(repository).update(itemCaptor.capture());
        assertThat(itemCaptor.getValue())
                .extracting(Item::getOwnerAccess)
                .isEqualTo(getExpected(ownerAccess));
        assertThat(itemCaptor.getValue())
                .extracting(Item::getGroupAccess)
                .isEqualTo(getExpected(groupAccess));
        assertThat(itemCaptor.getValue())
                .extracting(Item::getOtherAccess)
                .isEqualTo(getExpected(otherAccess));
    }

    private AccessRight getExpected(AccessRight right) {
        return ofNullable(right).orElse(AccessRight.none());
    }
}
