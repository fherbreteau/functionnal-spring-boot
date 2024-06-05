package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.driven.GroupRepository;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ActiveProfiles("test")
@ContextConfiguration(classes = RepositoryConfiguration.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcGroupRepositoryTest {

    private static final UUID ROOT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final String ROOT_NAME = "wheel";

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("postgresql")));

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void shouldCheckExistenceOfRootGroupByName() {
        assertThat(groupRepository.exists(ROOT_NAME)).isTrue();
    }

    @Test
    void shouldCheckExistenceOfRootGroupById() {
        assertThat(groupRepository.exists(ROOT_ID)).isTrue();
    }

    @Test
    void shouldExtractRootGroupFromName() {
        Group group = groupRepository.findByName(ROOT_NAME);
        assertThat(group)
                .extracting(Group::getGroupId, Group::getName)
                .containsExactly(ROOT_ID, ROOT_NAME);
    }

    @Test
    void shouldExtractRootGroupFromId() {
        Group group = groupRepository.findById(ROOT_ID);
        assertThat(group)
                .extracting(Group::getGroupId, Group::getName)
                .containsExactly(ROOT_ID, ROOT_NAME);
    }

    @Test
    void shouldDeleteExistingGroup() {
        UUID groupId = UUID.fromString("afa62e92-2239-40bf-a0d0-606f20e00b00");
        Group group = Group.builder("Test2").withGroupId(groupId).build();
        groupRepository.delete(group);
        assertThat(groupRepository.exists(groupId)).isFalse();
    }
}
