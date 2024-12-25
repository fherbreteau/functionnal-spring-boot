package io.github.fherbreteau.functional.driving.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.CompositeItemCommandFactory;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.path.CompositePathParserFactory;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driving.FileService;

public class FileServiceImpl implements FileService {

    private final CompositeItemCommandFactory commandFactory;

    private final CompositePathParserFactory pathFactory;

    public FileServiceImpl(CompositeItemCommandFactory commandFactory, CompositePathParserFactory pathFactory) {
        this.commandFactory = commandFactory;
        this.pathFactory = pathFactory;
    }

    public Path getPath(String path, User currentUser) {
        PathParser parser = pathFactory.createParser(Path.ROOT, path);
        return parser.resolve(currentUser);
    }

    @SuppressWarnings("unchecked")
    public <T> Output<T> processCommand(ItemCommandType type, User currentUser, ItemInput itemInput) {
        CheckCommand<T> command = commandFactory.createCommand(type, itemInput);
        return command.execute(currentUser).execute(currentUser);
    }
}
