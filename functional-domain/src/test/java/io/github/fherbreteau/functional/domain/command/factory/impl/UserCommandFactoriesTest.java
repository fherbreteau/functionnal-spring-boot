package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class UserCommandFactoriesTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserChecker userChecker;
    @Mock
    private PasswordProtector passwordProtector;
    @Mock
    private User actor;

    @Test
    void shouldCreateAUserWithGivenInformation() {
        UserCommandFactory factory = new UserAddCommandFactory();
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UserInput input = UserInput.builder("user")
                .withUserId(userId)
                .withGroupId(groupId)
                .withPassword("password")
                .build();

        Group group = Group.builder("group").withGroupId(groupId).build();

        given(userChecker.canCreateUser("user", actor)).willReturn(true);

        given(groupRepository.exists(groupId)).willReturn(true);
        given(groupRepository.findById(groupId)).willReturn(group);

        given(userRepository.exists("user")).willReturn(false);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(userRepository.updatePassword(any(), eq("password")))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(passwordProtector.validate("password")).willReturn(List.of());
        given(passwordProtector.protect("password")).willAnswer(invocation -> invocation.getArgument(0));

        CheckCommand<Output> checkCommand = factory.createCommand(userRepository, groupRepository, userChecker,
                passwordProtector, UserCommandType.USERADD, input);
        Command<Output> executeCommand = checkCommand.execute(actor);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getName)
                .isEqualTo("user");
        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getUserId)
                .isEqualTo(userId);
        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getGroup, type(Group.class))
                .isEqualTo(group);
        verify(userRepository).updatePassword(any(), eq("password"));
    }

    @Test
    void shouldModifyAUserWithGivenInformation() {
        UserCommandFactory factory = new UserModifyCommandFactory();
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        UserInput input = UserInput.builder("user")
                .withUserId(userId)
                .withGroupId(groupId)
                .withPassword("password")
                .withNewName("user1")
                .build();

        User user = User.builder("user").build();
        Group group = Group.builder("group").withGroupId(groupId).build();

        given(userChecker.canUpdateUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(true);
        given(userRepository.exists(userId)).willReturn(false);
        given(userRepository.exists("user1")).willReturn(false);
        given(userRepository.findByName("user")).willReturn(user);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(userRepository.updatePassword(any(), eq("password")))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(groupRepository.exists(groupId)).willReturn(true);
        given(groupRepository.findById(groupId)).willReturn(group);
        given(passwordProtector.validate("password")).willReturn(List.of());
        given(passwordProtector.protect("password")).willAnswer(invocation -> invocation.getArgument(0));

        CheckCommand<Output> checkCommand = factory.createCommand(userRepository, groupRepository, userChecker,
                passwordProtector, UserCommandType.USERMOD, input);
        Command<Output> executeCommand = checkCommand.execute(actor);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getName)
                .isEqualTo("user1");
        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getUserId)
                .isEqualTo(userId);
        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getGroup, type(Group.class))
                .isEqualTo(group);
        verify(userRepository).updatePassword(any(), eq("password"));
    }

    @Test
    void shouldModifyAUserWithGivenGroupName() {
        UserCommandFactory factory = new UserModifyCommandFactory();
        UUID groupId = UUID.randomUUID();
        UserInput input = UserInput.builder("user")
                .withGroups(List.of("group1"))
                .build();

        User user = User.builder("user").build();
        Group group = Group.builder("group1").withGroupId(groupId).build();

        given(userChecker.canUpdateUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(true);
        given(userRepository.findByName("user")).willReturn(user);
        given(groupRepository.exists("group1")).willReturn(true);
        given(groupRepository.findByName("group1")).willReturn(group);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        CheckCommand<Output> checkCommand = factory.createCommand(userRepository, groupRepository, userChecker,
                passwordProtector, UserCommandType.USERMOD, input);
        Command<Output> executeCommand = checkCommand.execute(actor);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getName)
                .isEqualTo("user");
        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getGroup, type(Group.class))
                .isEqualTo(group);
    }

    @Test
    void shouldDeleteAnUser() {
        UserCommandFactory factory = new UserDeleteCommandFactory();
        UserInput input = UserInput.builder("user").build();
        User user = User.builder("user").build();

        given(userChecker.canDeleteUser("user", actor)).willReturn(true);
        given(userRepository.exists("user")).willReturn(true);
        given(userRepository.findByName("user")).willReturn(user);
        given(userRepository.delete(user)).willAnswer(invocation -> invocation.getArgument(0));

        CheckCommand<Output> checkCommand = factory.createCommand(userRepository, groupRepository, userChecker,
                passwordProtector, UserCommandType.USERDEL, input);
        Command<Output> executeCommand = checkCommand.execute(actor);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getName)
                .isEqualTo("user");
    }

    @Test
    void shouldGetAUserByName() {
        UserCommandFactory factory = new UserGetCommandFactory();
        UserInput input = UserInput.builder("user").build();
        User user = User.builder("user").build();
        given(userRepository.exists("user")).willReturn(true);
        given(userRepository.findByName("user")).willReturn(user);

        CheckCommand<Output> checkCommand = factory.createCommand(userRepository, groupRepository, userChecker,
                passwordProtector, UserCommandType.ID, input);
        Command<Output> executeCommand = checkCommand.execute(actor);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(User.class))
                .isNotNull()
                .extracting(User::getName)
                .isEqualTo("user");
    }

    @Test
    void shouldCreateAGroupWithGivenInformation() {
        UserCommandFactory factory = new GroupAddCommandFactory();
        UUID groupId = UUID.randomUUID();
        UserInput input = UserInput.builder("group").withGroupId(groupId).build();

        given(userChecker.canCreateGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(false);
        given(groupRepository.exists(groupId)).willReturn(false);
        given(groupRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        CheckCommand<Output> checkCommand = factory.createCommand(userRepository, groupRepository, userChecker,
                passwordProtector, UserCommandType.GROUPADD, input);
        Command<Output> executeCommand = checkCommand.execute(actor);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(Group.class))
                .isNotNull()
                .extracting(Group::getName)
                .isEqualTo("group");
        assertThat(output).extracting(Output::getValue, type(Group.class))
                .isNotNull()
                .extracting(Group::getGroupId)
                .isEqualTo(groupId);
    }

    @Test
    void shouldModifyAGroupWithGivenInformation() {
        UserCommandFactory factory = new GroupModifyCommandFactory();
        UUID groupId = UUID.randomUUID();
        UserInput input = UserInput.builder("group").withGroupId(groupId).withNewName("group1").build();

        Group group = Group.builder("group").build();

        given(userChecker.canUpdateGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(groupRepository.exists("group1")).willReturn(false);
        given(groupRepository.exists(groupId)).willReturn(false);
        given(groupRepository.findByName("group")).willReturn(group);
        given(groupRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        CheckCommand<Output> checkCommand = factory.createCommand(userRepository, groupRepository, userChecker,
                passwordProtector, UserCommandType.GROUPMOD, input);
        Command<Output> executeCommand = checkCommand.execute(actor);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(Group.class))
                .isNotNull()
                .extracting(Group::getName)
                .isEqualTo("group1");
        assertThat(output).extracting(Output::getValue, type(Group.class))
                .isNotNull()
                .extracting(Group::getGroupId)
                .isEqualTo(groupId);
    }

    @Test
    void shouldDeleteAGroup() {
        UserCommandFactory factory = new GroupDeleteCommandFactory();
        UserInput input = UserInput.builder("group").withForce(true).build();
        Group group = Group.builder("group").build();

        given(userChecker.canDeleteGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(groupRepository.findByName("group")).willReturn(group);
        given(userRepository.removeGroupFromUser(group)).willAnswer(invocation -> invocation.getArgument(0));
        given(groupRepository.delete(group)).willAnswer(invocation -> invocation.getArgument(0));

        CheckCommand<Output> checkCommand = factory.createCommand(userRepository, groupRepository, userChecker,
                passwordProtector, UserCommandType.GROUPDEL, input);
        Command<Output> executeCommand = checkCommand.execute(actor);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, type(Group.class))
                .isNotNull()
                .extracting(Group::getName)
                .isEqualTo("group");
    }

    @Test
    void shouldGetGroupsOfAUserByName() {
        UserCommandFactory factory = new GroupGetCommandFactory();
        UserInput input = UserInput.builder("user").build();
        User user = User.builder("user").withGroup(Group.builder("group").build()).build();
        given(userRepository.exists("user")).willReturn(true);
        given(userRepository.findByName("user")).willReturn(user);

        CheckCommand<Output> checkCommand = factory.createCommand(userRepository, groupRepository, userChecker,
                passwordProtector, UserCommandType.GROUPS, input);
        Command<Output> executeCommand = checkCommand.execute(actor);
        Output output = executeCommand.execute(actor);

        assertThat(output).extracting(Output::getValue, list(Group.class))
                .isNotNull()
                .hasSize(1)
                .first()
                .extracting(Group::getName)
                .isEqualTo("group");
    }
}
