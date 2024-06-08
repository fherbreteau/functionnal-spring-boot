package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.infra.config.RepositoryConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@ActiveProfiles("test")
@ContextConfiguration(classes = RepositoryConfiguration.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcItemRepositoryTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("postgresql")));

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void shouldCheckExistenceOfGivenFile() {
        assertTrue(itemRepository.exists(Folder.getRoot(), "folder"));
    }

    @Test
    void shouldCreateItemInDatabase() {
        File file = File.builder()
                .withName("file")
                .withOwner(User.root())
                .withParent(Folder.getRoot())
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.none())
                .withOtherAccess(AccessRight.none())
                .withContentType("content-type")
                .build();
        assertThat(itemRepository.create(file))
                .extracting(AbstractItem::getHandle)
                .isNotNull();
        assertThat(itemRepository.exists(Folder.getRoot(), "file"))
                .isTrue();
    }

    @Test
    void shouldUpdateItemInDatabase() {
        Folder folder = itemRepository.findByNameAndParentAndUser("folder", Folder.getRoot(), User.root())
                .map(Folder.class::cast).orElseThrow();
        folder = folder.copyBuilder().withName("folder2").build();
        assertThat(itemRepository.update(folder)).isNotNull();
        assertThat(itemRepository.exists(Folder.getRoot(), "folder"))
                .isFalse();
    }

    @Test
    void shouldUpdateItemAccessRight() {
        Folder folder = itemRepository.findByNameAndParentAndUser("folder", Folder.getRoot(), User.root())
                .map(Folder.class::cast).orElseThrow();
        folder = folder.copyBuilder()
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.full())
                .withOtherAccess(AccessRight.full())
                .build();
        assertThat(itemRepository.update(folder)).isNotNull();
        assertThat(itemRepository.exists(Folder.getRoot(), "folder"))
                .isTrue();
        assertThat(itemRepository.findByNameAndParentAndUser("folder", Folder.getRoot(), User.root()))
                .isPresent().contains(folder);
    }

    @Test
    void shouldDeleteItemInDatabase() {
        Folder folder = itemRepository.findByNameAndParentAndUser("folder", Folder.getRoot(), User.root())
                .map(Folder.class::cast).orElseThrow();
        File file = itemRepository.findByNameAndParentAndUser("to_delete", folder, User.root())
                .map(File.class::cast).orElseThrow();
        itemRepository.delete(file);
        assertThat(itemRepository.exists(folder, "to_delete"))
                .isFalse();
    }

    @Test
    void shouldListItemsInFolder() {
        List<Item> children = itemRepository.findByParentAndUser(Folder.getRoot(), User.root());
        assertThat(children).hasSizeGreaterThanOrEqualTo(1)
                .extracting(Item::getName)
                .containsOnlyOnce("folder");
    }

    @Test
    void shouldFindItemInFolder() {
        Optional<Item> found = itemRepository.findByNameAndParentAndUser("folder", Folder.getRoot(), User.root());
        assertThat(found).isPresent()
                .map(Item::getName)
                .hasValue("folder");
    }
}
