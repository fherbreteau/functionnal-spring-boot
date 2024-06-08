package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.infra.config.RepositoryConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

@JdbcTest
@ActiveProfiles("test")
@ContextConfiguration(classes = RepositoryConfiguration.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcUserRepositoryTest {

    private static final UUID ROOT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final String ROOT_NAME = "root";

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("postgresql")));

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCheckExistenceOfRootUserByName() {
        assertThat(userRepository.exists(ROOT_NAME)).isTrue();
    }

    @Test
    void shouldCheckExistenceOfRootUserById() {
        assertThat(userRepository.exists(ROOT_ID)).isTrue();
    }

    @Test
    void shouldExtractRootUserFromName() {
        User user = userRepository.findByName(ROOT_NAME);
        assertThat(user)
                .extracting(User::getUserId, User::getName)
                .containsExactly(ROOT_ID, ROOT_NAME);
        assertThat(user)
                .extracting(User::getGroups, list(Group.class))
                .singleElement()
                .isEqualTo(Group.builder("wheel").withGroupId(ROOT_ID).build());
    }

    @Test
    void shouldExtractRootUserFromId() {
        User user = userRepository.findById(ROOT_ID);
        assertThat(user)
                .extracting(User::getUserId, User::getName)
                .containsExactly(ROOT_ID, ROOT_NAME);
        assertThat(user)
                .extracting(User::getGroups, list(Group.class))
                .singleElement()
                .isEqualTo(Group.builder("wheel").withGroupId(ROOT_ID).build());
    }

    @Test
    void shouldCreateAnUser() {
        UUID userId = UUID.fromString("987210f6-ce79-4a4c-9222-54bcd34a8ee2");
        UUID group1Id = UUID.fromString("22bdb905-73d4-479e-99fc-62d46ad27d67");
        UUID group2Id = UUID.fromString("afa62e92-2239-40bf-a0d0-606f20e00b00");
        UUID group3Id = UUID.fromString("bc321002-b703-424b-9c3f-d47bf15be632");
        User userToCreate = User.builder("Test3")
                .withUserId(userId)
                .withGroups(List.of(
                        Group.builder("Test").withGroupId(group1Id).build(),
                        Group.builder("Test2").withGroupId(group2Id).build(),
                        Group.builder("User").withGroupId(group3Id).build()
                ))
                .build();
        assertThat(userRepository.create(userToCreate)).isEqualTo(userToCreate);
        assertThat(userRepository.exists("Test3")).isTrue();
        assertThat(userRepository.exists(userId)).isTrue();

        assertThat(userRepository.findByName("Test3")).isEqualTo(userToCreate);
    }

    @Test
    void shouldUpdateAnExistingUser() {
        UUID userId = UUID.fromString("bc321002-b703-424b-9c3f-d47bf15be632");
        User userToUpdate = User.builder("User")
                .withUserId(userId)
                .build()
                .copy().withName("User2").build();
        assertThat(userRepository.update(userToUpdate)).isEqualTo(userToUpdate);
        assertThat(userRepository.exists("User")).isFalse();
        assertThat(userRepository.exists(userId)).isTrue();

        UUID newUUID = UUID.fromString("4a9e0664-df93-433a-a953-c59826abc89b");
        userToUpdate = userToUpdate.copy().withUserId(newUUID).build();
        assertThat(userRepository.update(userToUpdate)).isEqualTo(userToUpdate);
        assertThat(userRepository.exists("User2")).isTrue();
        assertThat(userRepository.exists(userId)).isFalse();

        assertThat(userRepository.update(userToUpdate)).isEqualTo(userToUpdate);
    }

    @Test
    void shouldDeleteExistingUser() {
        UUID userId = UUID.fromString("1115a887-fec2-44fe-9f4d-73d8d6fec46b");
        User user = User.builder("Test2").withUserId(userId).build();
        userRepository.delete(user);
        assertThat(userRepository.exists(userId)).isFalse();
    }

    @Test
    void shouldLookForUserWithASpecificGroupName() {
        assertThat(userRepository.hasUserWithGroup("Test3")).isFalse();
        assertThat(userRepository.hasUserWithGroup("wheel")).isTrue();
    }

    @Test
    void shouldValidateUserPassword() {
        UUID userId = UUID.fromString("bc321002-b703-424b-9c3f-d47bf15be632");
        User user = User.builder("User").withUserId(userId).build();
        assertThat(userRepository.checkPassword(user, "password")).isTrue();
        assertThat(userRepository.checkPassword(user, "")).isFalse();
    }

    @Test
    void shouldUpdateUserPassword() {
        UUID userId = UUID.fromString("22bdb905-73d4-479e-99fc-62d46ad27d67");
        User user = User.builder("Test").withUserId(userId).build();
        assertThat(userRepository.updatePassword(user, "password")).isEqualTo(user);
        assertThat(userRepository.checkPassword(user, "password")).isTrue();
    }

    @Test
    void shouldRemoveGroupFromUser() {
        UUID groupId = UUID.fromString("22bdb905-73d4-479e-99fc-62d46ad27d67");
        Group group = Group.builder("Test").withGroupId(groupId).build();
        userRepository.removeGroupFromUser(group);
        assertThat(userRepository.findByName("Test"))
                .extracting(User::getGroups, list(Group.class))
                .singleElement()
                .isEqualTo(Group.builder("User")
                        .withGroupId(UUID.fromString("bc321002-b703-424b-9c3f-d47bf15be632"))
                        .build());
    }
}
