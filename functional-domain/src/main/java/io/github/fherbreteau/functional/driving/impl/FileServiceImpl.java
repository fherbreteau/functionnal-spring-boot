package io.github.fherbreteau.functional.driving.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.CompositeItemCommandFactory;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.path.CompositePathParserFactory;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driving.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileServiceImpl implements FileService {
    private final Logger logger = LoggerFactory.getLogger(FileService.class.getSimpleName());

    private final CompositeItemCommandFactory commandFactory;
    private final CompositePathParserFactory pathFactory;

    public FileServiceImpl(CompositeItemCommandFactory commandFactory, CompositePathParserFactory pathFactory) {
        this.commandFactory = commandFactory;
        this.pathFactory = pathFactory;
    }

    public Path getPath(String path, User currentUser) {
        logger.debug("Getting path {} for {}", path, currentUser);
        PathParser parser = pathFactory.createParser(Path.ROOT, path);
        return parser.resolve(currentUser);
    }

    @SuppressWarnings("unchecked")
    public <T> Output<T> processCommand(ItemCommandType type, User currentUser, ItemInput input) {
        logger.debug("Processing command {} for {}", type, currentUser);
        CheckCommand<T> command = commandFactory.createCommand(type, input);
        return command.execute(currentUser).execute(currentUser);
    }
}
