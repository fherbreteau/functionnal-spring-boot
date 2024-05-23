package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserInput;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserAddCommandTest {
    private UserAddCommand command;
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
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    public void setup() {
        UserInput input = UserInput.builder("user").build();
        command = new UserAddCommand(userRepository, groupRepository, passwordProtector, input);
    }

    @Test
    void shouldCreateUserAndCreateGroupWhenExecutingCommand() {
        // GIVEN
        given(groupRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        // WHEN
        Output result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(groupRepository).save(groupCaptor.capture());
        assertThat(groupCaptor.getValue())
                .extracting(Group::getGroupId)
                .isNotNull();
        assertThat(groupCaptor.getValue())
                .extracting(Group::getName)
                .isEqualTo("user");
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue())
                .extracting(User::getUserId)
                .isNotNull();
        assertThat(userCaptor.getValue())
                .extracting(User::getName)
                .isEqualTo("user");
    }

    @Test
    void shouldCreateUserAndUseExistingGroupWhenExecutingCommand() {
        // GIVEN
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        Group group = Group.builder("group").build();
        given(groupRepository.findByName("group")).willReturn(group);
        UserInput input = UserInput.builder("user").withGroups(List.of("group")).build();
        command = new UserAddCommand(userRepository, groupRepository, passwordProtector, input);
        // WHEN
        Output result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue())
                .extracting(User::getUserId)
                .isNotNull();
        assertThat(userCaptor.getValue())
                .extracting(User::getName)
                .isEqualTo("user");
        assertThat(userCaptor.getValue())
                .extracting(User::getGroup)
                .isEqualTo(group);
    }
}
