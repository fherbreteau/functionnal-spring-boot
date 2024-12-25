package io.github.fherbreteau.functional.infra.impl;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FSContentRepository implements ContentRepository, InitializingBean {

    private static final String FILE_FORMAT = "%s.dat";

    private final Path rootPath;

    @Autowired
    public FSContentRepository(@Value("${content.repository.path}") String rootPath) {
        this(rootPath, FileSystems.getDefault());
    }

    FSContentRepository(String rootPath, FileSystem fs) {
        this.rootPath = fs.getPath(rootPath);
    }

    @Override
    public Output<Item> initContent(File item) {
        Path itemPath = getItemPath(item);
        try {
            Files.createFile(itemPath);
            return Output.success(item);
        } catch (IOException e) {
            return Output.failure(e);
        }
    }

    @Override
    public Output<InputStream> readContent(File item) {
        Path itemPath = getItemPath(item);
        try {
            return Output.success(Files.newInputStream(itemPath, READ));
        } catch (IOException e) {
            return Output.failure(e);
        }
    }

    @Override
    public Output<Item> writeContent(File item, InputStream content) {
        Path itemPath = getItemPath(item);
        try (OutputStream oStream = Files.newOutputStream(itemPath, TRUNCATE_EXISTING, WRITE)) {
            content.transferTo(oStream);
            return Output.success(item);
        } catch (IOException e) {
            return Output.failure(e);
        }
    }

    @Override
    public Output<Void> deleteContent(File item) {
        Path itemPath = getItemPath(item);
        try {
            Files.delete(itemPath);
            return Output.success(null);
        } catch (IOException e) {
            return Output.failure(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Files.notExists(this.rootPath)) {
            Files.createDirectory(this.rootPath);
        }
    }

    private Path getItemPath(File item) {
        return rootPath.resolve(String.format(FILE_FORMAT, item.getHandle()));
    }
}
