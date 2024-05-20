package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
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
class UserModifyCommandTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private User actor;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void shouldUpdateModifyUserWhenExecutingCommand() {
        UUID groupId = UUID.randomUUID();
        UserInput input = UserInput.builder("user").withGroupId(groupId).withGroupName("group1").build();
        UserModifyCommand command = new UserModifyCommand(userRepository, groupRepository, input);
        // GIVEN
        Group group = Group.builder("group1").withGroupId(groupId).build();
        User user = User.builder("user").withGroup(Group.builder("group").build()).build();
        given(userRepository.findByName("user")).willReturn(user);
        given(groupRepository.findByNameAndId("group1", groupId)).willReturn(group);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        // WHEN
        Output result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue())
                .extracting(User::getGroup)
                .extracting(Group::getGroupId)
                .isEqualTo(groupId);
        assertThat(userCaptor.getValue())
                .extracting(User::getGroup)
                .extracting(Group::getName)
                .isEqualTo("group1");
    }

    @Test
    void shouldUpdateModifyUserWhenExecutingCommand1() {
        UserInput input = UserInput.builder("user").build();
        UserModifyCommand command = new UserModifyCommand(userRepository, groupRepository, input);
        // GIVEN
        User user = User.builder("user").withGroup(Group.builder("group").build()).build();
        given(userRepository.findByName("user")).willReturn(user);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        // WHEN
        Output result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue())
                .isEqualTo(user);
    }
}
