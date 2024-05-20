package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.command.factory.impl.*;
import io.github.fherbreteau.functional.domain.command.impl.check.*;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

@ExtendWith(MockitoExtension.class)
class CompositeUserCommandFactoryTest {

    private CompositeUserCommandFactory factory;
    @Mock
    private UserChecker userChecker;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;

    public static Stream<Arguments> validCommandArguments() {
        return Stream.of(
                // USERADD Command
                Arguments.of(UserCommandType.USERADD, UserInput.builder("user").build(), CheckUserAddCommand.class),
                Arguments.of(UserCommandType.USERADD, UserInput.builder("user").withUserId(UUID.randomUUID()).build(), CheckUserAddCommand.class),
                Arguments.of(UserCommandType.USERADD, UserInput.builder("user").withGroupId(UUID.randomUUID()).build(), CheckUserAddCommand.class),
                Arguments.of(UserCommandType.USERADD, UserInput.builder("user").withPassword("password").build(), CheckUserAddCommand.class),
                // USERMOD Command
                Arguments.of(UserCommandType.USERMOD, UserInput.builder("user").withGroupName("group").build(), CheckUserModifyCommand.class),
                Arguments.of(UserCommandType.USERMOD, UserInput.builder("user").withGroupId(UUID.randomUUID()).build(), CheckUserModifyCommand.class),
                Arguments.of(UserCommandType.USERMOD, UserInput.builder("user").withUserId(UUID.randomUUID()).build(), CheckUserModifyCommand.class),
                Arguments.of(UserCommandType.USERMOD, UserInput.builder("user").withNewName("user2").build(), CheckUserModifyCommand.class),
                Arguments.of(UserCommandType.USERMOD, UserInput.builder("user").withPassword("password").build(), CheckUserModifyCommand.class),
                // USERDEL Command
                Arguments.of(UserCommandType.USERDEL, UserInput.builder("user").build(), CheckUserDeleteCommand.class),
                Arguments.of(UserCommandType.USERDEL, UserInput.builder("user").withForce(true).build(), CheckUserDeleteCommand.class),
                // PASSWD Command
                Arguments.of(UserCommandType.PASSWD, UserInput.builder("user").withPassword("password").build(), CheckUserModifyCommand.class),
                // GROUPADD Command
                Arguments.of(UserCommandType.GROUPADD, UserInput.builder("group").build(), CheckGroupAddCommand.class),
                Arguments.of(UserCommandType.GROUPADD, UserInput.builder("group").withGroupId(UUID.randomUUID()).build(), CheckGroupAddCommand.class),
                // GROUPMOD Command
                Arguments.of(UserCommandType.GROUPMOD, UserInput.builder("group").withNewName("group").build(), CheckGroupModifyCommand.class),
                Arguments.of(UserCommandType.GROUPMOD, UserInput.builder("group").withGroupId(UUID.randomUUID()).build(), CheckGroupModifyCommand.class),
                // GROUPDEL Command
                Arguments.of(UserCommandType.GROUPDEL, UserInput.builder("group").build(), CheckGroupDeleteCommand.class),
                Arguments.of(UserCommandType.GROUPDEL, UserInput.builder("group").withForce(true).build(), CheckGroupDeleteCommand.class)
        );
    }

    public static Stream<Arguments> invalidCommandArguments() {
        return Stream.of(
                Arguments.of(UserCommandType.USERADD, UserInput.builder(null).build()),
                Arguments.of(UserCommandType.USERMOD, UserInput.builder(null).build()),
                Arguments.of(UserCommandType.USERMOD, UserInput.builder("name").build()),
                Arguments.of(UserCommandType.USERDEL, UserInput.builder(null).build()),
                Arguments.of(UserCommandType.PASSWD, UserInput.builder(null).build()),
                Arguments.of(UserCommandType.PASSWD, UserInput.builder("user").build()),
                Arguments.of(UserCommandType.GROUPADD, UserInput.builder(null).build()),
                Arguments.of(UserCommandType.GROUPMOD, UserInput.builder(null).build()),
                Arguments.of(UserCommandType.GROUPMOD, UserInput.builder("name").build()),
                Arguments.of(UserCommandType.GROUPDEL, UserInput.builder(null).build())
        );
    }

    @BeforeEach
    public void setup() {
        List<UserCommandFactory> factories = List.of(
                new GroupAddCommandFactory(),
                new GroupDeleteCommandFactory(),
                new GroupModifyCommandFactory(),
                new UnsupportedUserCommandFactory(),
                new UserAddCommandFactory(),
                new UserDeleteCommandFactory(),
                new UserModifyCommandFactory()
        );
        factory = new CompositeUserCommandFactory(userRepository, groupRepository, userChecker, factories);
    }

    @ParameterizedTest(name = "Command of {0} with args {1} is supported")
    @MethodSource("validCommandArguments")
    void testCommandCreatedForSpecificTypeAndValidInput(UserCommandType type, UserInput itemInput, Class<? extends CheckCommand<Output>> expected) {
        Command<?> command = factory.createCommand(type, itemInput);

        assertThat(command).isNotNull().isInstanceOf(expected);
    }

    @ParameterizedTest(name = "Command of {0} with args {1} is not supported")
    @MethodSource("invalidCommandArguments")
    void testCommandCreatedForSpecificTypeAndInvalidInput(UserCommandType type, UserInput itemInput) {
        Command<?> command = factory.createCommand(type, itemInput);

        assertThat(command).isNotNull().isInstanceOf(CheckUnsupportedUserCommand.class);
    }

    @Test
    void testOrderOfCommandFactoriesIsRespected() {
        List<ItemCommandFactory> factories = List.of(
                new UnsupportedItemCommandFactory(),
                new ListChildrenCommandFactory(),
                new UploadCommandFactory()
        );
        List<ItemCommandFactory> sortedFactories = factories.stream().sorted(Comparator.comparing(ItemCommandFactory::order)).toList();
        assertThat(sortedFactories).last(type(ItemCommandFactory.class))
                .isInstanceOf(UnsupportedItemCommandFactory.class);

    }
}
