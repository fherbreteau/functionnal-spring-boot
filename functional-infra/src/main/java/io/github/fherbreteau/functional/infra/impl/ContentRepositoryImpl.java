package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.infra.ItemIdFinder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Repository
public class ContentRepositoryImpl implements ContentRepository, InitializingBean {

    private static final String FILE_FORMAT = "%s.dat";

    private final Path rootPath;
    private final ItemIdFinder itemIdFinder;

    @Autowired
    public ContentRepositoryImpl(@Value("content.repository.path") String rootPath,
                                 ItemIdFinder itemIdFinder) {
        this(rootPath, itemIdFinder, FileSystems.getDefault());
    }

    ContentRepositoryImpl(String rootPath, ItemIdFinder itemIdFinder, FileSystem fs) {
        this.rootPath = fs.getPath(rootPath);
        this.itemIdFinder = itemIdFinder;
    }

    @Override
    public Output<Item> initContent(File item) {
        Path itemPath = getItemPath(item);
        try {
            Files.createFile(itemPath);
            return Output.success(item);
        } catch (IOException e) {
            return Output.error(e);
        }
    }

    @Override
    public Output<InputStream> readContent(File item) {
        Path itemPath = getItemPath(item);
        try {
            return Output.success(Files.newInputStream(itemPath, StandardOpenOption.READ));
        } catch (IOException e) {
            return Output.error(e);
        }
    }

    @Override
    public Output<Item> writeContent(File item, InputStream content) {
        Path itemPath = getItemPath(item);
        try {
            Files.copy(content, itemPath, StandardCopyOption.REPLACE_EXISTING);
            return Output.success(item);
        } catch (IOException e) {
            return Output.error(e);
        }
    }

    @Override
    public Output<Void> deleteContent(File item) {
        Path itemPath = getItemPath(item);
        try {
            Files.delete(itemPath);
            return Output.success(null);
        } catch (IOException e) {
            return Output.error(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Files.notExists(this.rootPath)) {
            Files.createDirectory(this.rootPath);
        }
    }

    private Path getItemPath(File item) {
        UUID itemId = itemIdFinder.getItemId(item);
        return rootPath.resolve(String.format(FILE_FORMAT, itemId.toString()));
    }
}
