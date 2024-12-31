package io.github.fherbreteau.functional.driving.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.CompositeItemCommandFactory;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.CompositePathParserFactory;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driving.FileService;

public class FileServiceImpl implements FileService {
    private final Logger logger = Logger.getLogger(FileService.class.getSimpleName());

    private final CompositeItemCommandFactory commandFactory;
    private final CompositePathParserFactory pathFactory;

    public FileServiceImpl(CompositeItemCommandFactory commandFactory, CompositePathParserFactory pathFactory) {
        this.commandFactory = commandFactory;
        this.pathFactory = pathFactory;
    }

    public Path getPath(String path, User currentUser) {
        debug(logger,  "Getting path {0} for {1}", path, currentUser);
        PathParser parser = pathFactory.createParser(Path.ROOT, path);
        return parser.resolve(currentUser);
    }

    @SuppressWarnings("unchecked")
    public <T> Output<T> processCommand(ItemCommandType type, User currentUser, ItemInput input) {
        debug(logger,  "Processing command {0} for {1} on {2}", type, currentUser, input);
        CheckCommand<T> command = commandFactory.createCommand(type, input);
        return command.execute(currentUser).execute(currentUser);
    }
}
