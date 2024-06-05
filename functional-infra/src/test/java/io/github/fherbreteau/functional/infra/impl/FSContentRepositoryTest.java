package io.github.fherbreteau.functional.infra.impl;

import com.google.common.jimfs.Jimfs;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.infra.ItemFinder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.InitializingBean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FSContentRepositoryTest {
    private static final String ROOT_PATH = "/data";

    private ContentRepository repository;

    @Mock
    private ItemFinder finder;

    private final UUID fakeUUID = UUID.randomUUID();

    private final FileSystem fs = Jimfs.newFileSystem();

    @BeforeEach
    public void setup() throws Exception {
        repository = new FSContentRepository(ROOT_PATH, finder, fs);
        ((InitializingBean) repository).afterPropertiesSet();
        when(finder.getItemId(any())).thenReturn(fakeUUID);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Path basePath = fs.getPath(ROOT_PATH);
        if (Files.notExists(basePath)) {
            return;
        }
        try (Stream<Path> stream = Files.list(basePath)) {
            stream.forEach(this::delete);
        }
    }

    private void delete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void shouldCreateAFileWithTheExpectedNameOnTheFileSystemWhenInitializingContent() {
        File file = File.builder().withName("name").withParent(Folder.getRoot()).build();
        Output<Item> result = repository.initContent(file);
        assertThat(result).extracting(Output::isSuccess, BOOLEAN)
                .isTrue();
        Path filePath = fs.getPath(ROOT_PATH, fakeUUID + ".dat");
        assertThat(filePath).exists();
    }

    @Test
    void shouldReadAFileWithTheExpectedNameOnFileSystemWhenReadingContent() throws IOException {
        Path filePath = fs.getPath(ROOT_PATH, fakeUUID + ".dat");
        Files.copy(new ByteArrayInputStream("content".getBytes(StandardCharsets.UTF_8)), filePath);
        File file = File.builder().withName("name").withParent(Folder.getRoot()).build();
        Output<InputStream> result = repository.readContent(file);
        assertThat(result).extracting(Output::isSuccess, BOOLEAN)
                .isTrue();
        assertThat(result).extracting(Output::getValue, INPUT_STREAM)
                .asString(StandardCharsets.UTF_8)
                .isEqualTo("content");
    }

    @Test
    void shouldWriteContentToFileWithExpectedNameOnFileSystemWhenWritingContent() throws IOException {
        Path filePath = Files.createFile(fs.getPath(ROOT_PATH, fakeUUID + ".dat"));
        File file = File.builder().withName("name").withParent(Folder.getRoot()).build();
        InputStream input = new ByteArrayInputStream("content".getBytes(StandardCharsets.UTF_8));
        Output<Item> result = repository.writeContent(file, input);
        assertThat(result).extracting(Output::isSuccess, BOOLEAN)
                .isTrue();
        assertThat(result).extracting(Output::getValue)
                .isEqualTo(file);
        assertThat(filePath).content(StandardCharsets.UTF_8)
                .isEqualTo("content");
    }

    @Test
    void shouldDeleteAFileWithExpectedNameOnFileSystemWhenDeletingContent() throws IOException {
        Path filePath = Files.createFile(fs.getPath(ROOT_PATH, fakeUUID + ".dat"));
        File file = File.builder().withName("name").withParent(Folder.getRoot()).build();
        Output<Void> result = repository.deleteContent(file);
        assertThat(result).extracting(Output::isSuccess, BOOLEAN)
                .isTrue();
        assertThat(filePath).doesNotExist();
    }

    @Test
    void shouldFailCreateAsRootContainerDoesNotExists() throws IOException {
        Files.createFile(fs.getPath(ROOT_PATH, fakeUUID + ".dat"));
        File file = File.builder().withName("name").withParent(Folder.getRoot()).build();
        Output<Item> result = repository.initContent(file);
        assertThat(result).extracting(Output::isError, BOOLEAN)
                .isTrue();
    }

    @Test
    void shouldFailReadAsRootContainerDoesNotExists() {
        File file = File.builder().withName("name").withParent(Folder.getRoot()).build();
        Output<InputStream> result = repository.readContent(file);
        assertThat(result).extracting(Output::isError, BOOLEAN)
                .isTrue();
    }

    @Test
    void shouldFailWriteAsRootContainerDoesNotExists() {
        File file = File.builder().withName("name").withParent(Folder.getRoot()).build();
        InputStream input = new ByteArrayInputStream("content".getBytes(StandardCharsets.UTF_8));
        Output<Item> result = repository.writeContent(file, input);
        assertThat(result).extracting(Output::isError, BOOLEAN)
                .isTrue();
    }

    @Test
    void shouldFailDeleteAsRootContainerDoesNotExists() {
        File file = File.builder().withName("name").withParent(Folder.getRoot()).build();
        Output<Void> result = repository.deleteContent(file);
        assertThat(result).extracting(Output::isError, BOOLEAN)
                .isTrue();
    }
}
