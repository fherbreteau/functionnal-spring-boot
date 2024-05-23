package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
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
    private PasswordProtector passwordProtector;
    @Mock
    private User actor;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void shouldUpdateModifyUserWhenExecutingCommand() {
        UUID groupId = UUID.randomUUID();
        UserInput input = UserInput.builder("user")
                .withGroupId(groupId)
                .withGroups(List.of("group1"))
                .withAppend(true)
                .build();
        UserModifyCommand command = new UserModifyCommand(userRepository, groupRepository, passwordProtector, input);
        // GIVEN
        Group group = Group.builder("group").withGroupId(groupId).build();
        Group group1 = Group.builder("group1").build();
        User user = User.builder("user").build();
        given(userRepository.findByName("user")).willReturn(user);
        given(groupRepository.findById(groupId)).willReturn(group);
        given(groupRepository.findByName("group1")).willReturn(group1);
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
                .isEqualTo("group");
        assertThat(userCaptor.getValue())
                .extracting(User::getGroups, list(Group.class))
                .hasSize(2)
                .last(type(Group.class))
                .extracting(Group::getName)
                .isEqualTo("group1");
    }

    @Test
    void shouldUpdateModifyUserWhenExecutingCommand1() {
        UserInput input = UserInput.builder("user").build();
        UserModifyCommand command = new UserModifyCommand(userRepository, groupRepository, passwordProtector, input);
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
