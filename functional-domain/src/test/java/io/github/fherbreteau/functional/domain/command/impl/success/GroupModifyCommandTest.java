package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GroupModifyCommandTest {
    private GroupModifyCommand command;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private PasswordProtector passwordProtector;
    @Mock
    private User actor;

    @Captor
    private ArgumentCaptor<Group> groupCaptor;

    private UUID groupId;

    @BeforeEach
    public void setup() {
        groupId = UUID.randomUUID();
        command = new GroupModifyCommand(userRepository, groupRepository, passwordProtector, "group", null, null);
    }

    @Test
    void shouldUpdateAddGroupWhenExecutingCommand() {
        // GIVEN
        Group group = Group.builder("group").withGroupId(groupId).build();
        given(groupRepository.findByName("group")).willReturn(group);
        given(groupRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        // WHEN
        Output result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(groupRepository).save(groupCaptor.capture());
        assertThat(groupCaptor.getValue())
                .extracting(Group::getGroupId)
                .isEqualTo(groupId);
        assertThat(groupCaptor.getValue())
                .extracting(Group::getName)
                .isEqualTo("group");
    }
}
