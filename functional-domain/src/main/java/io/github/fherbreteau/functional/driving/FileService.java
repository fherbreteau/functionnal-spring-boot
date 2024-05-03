package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.command.*;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.path.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.PathParser;

public class FileService {

    private final CompositeCommandFactory commandFactory;

    private final CompositePathFactory pathFactory;

    public FileService(CompositeCommandFactory commandFactory, CompositePathFactory pathFactory) {
        this.commandFactory = commandFactory;
        this.pathFactory = pathFactory;
    }

    public Path getPath(String path, User currentUser) {
        PathParser parser = pathFactory.createParser(Path.ROOT, path);
        return parser.resolve(currentUser);
    }

    public Output processCommand(CommandType type, User currentUser, Input input) {
        Command<Command<Output>> command = commandFactory.createCommand(type, input);
        return command.execute(currentUser).execute(currentUser);
    }
}
