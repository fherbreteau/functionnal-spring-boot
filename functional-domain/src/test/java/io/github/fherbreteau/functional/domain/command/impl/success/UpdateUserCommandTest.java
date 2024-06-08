package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateUserCommandTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private PasswordProtector passwordProtector;
    @Mock
    private UserUpdater userUpdater;
    @Mock
    private User actor;

    @Captor
    private ArgumentCaptor<User> oldUserCaptor;
    @Captor
    private ArgumentCaptor<User> newUserCaptor;

    @Test
    void shouldUpdateModifyUserWhenExecutingCommand() {
        UUID groupId = UUID.randomUUID();
        UserInput input = UserInput.builder("user")
                .withGroupId(groupId)
                .withGroups(List.of("group1"))
                .withAppend(true)
                .build();
        UpdateUserCommand command = new UpdateUserCommand(userRepository, groupRepository, userUpdater,
                passwordProtector, input);
        // GIVEN
        Group group = Group.builder("group").withGroupId(groupId).build();
        Group group1 = Group.builder("group1").build();
        User user = User.builder("user").build();
        given(userRepository.findByName("user")).willReturn(user);
        given(groupRepository.findById(groupId)).willReturn(group);
        given(groupRepository.findByName("group1")).willReturn(group1);
        given(userRepository.update(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(userUpdater.updateUser(any(), any())).willAnswer(invocation -> invocation.getArgument(1));
        // WHEN
        Output<User> result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(userRepository).update(newUserCaptor.capture());
        assertThat(newUserCaptor.getValue())
                .extracting(User::getGroup)
                .extracting(Group::getGroupId)
                .isEqualTo(groupId);
        assertThat(newUserCaptor.getValue())
                .extracting(User::getGroup)
                .extracting(Group::getName)
                .isEqualTo("group");
        assertThat(newUserCaptor.getValue())
                .extracting(User::getGroups, list(Group.class))
                .hasSize(2)
                .last(type(Group.class))
                .extracting(Group::getName)
                .isEqualTo("group1");
        verify(userUpdater).updateUser(oldUserCaptor.capture(), eq(newUserCaptor.getValue()));
        assertThat(oldUserCaptor.getValue())
                .isEqualTo(user);
    }

    @Test
    void shouldUpdateModifyUserWhenExecutingCommand1() {
        UserInput input = UserInput.builder("user").withPassword("Password").build();
        UpdateUserCommand command = new UpdateUserCommand(userRepository, groupRepository, userUpdater, passwordProtector, input);
        // GIVEN
        User user = User.builder("user").withGroup(Group.builder("group").build()).build();
        given(userRepository.findByName("user")).willReturn(user);
        given(userRepository.update(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(userUpdater.updateUser(any(), any())).willAnswer(invocation -> invocation.getArgument(1));
        given(passwordProtector.protect(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(userRepository.updatePassword(any(), eq("Password"))).willAnswer(invocation -> invocation.getArgument(0));
        // WHEN
        Output<User> result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(userRepository).update(newUserCaptor.capture());
        assertThat(newUserCaptor.getValue())
                .isEqualTo(user);
    }
}
