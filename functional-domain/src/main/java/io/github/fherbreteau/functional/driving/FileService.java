package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.command.*;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.Path;
import io.github.fherbreteau.functional.domain.path.PathFactory;

import java.util.Objects;

public class FileService {

    private final CompositeFactory commandFactory;

    private final PathFactory pathFactory;

    public FileService(CompositeFactory commandFactory, PathFactory pathFactory) {
        this.commandFactory = commandFactory;
        this.pathFactory = pathFactory;
    }


    public Path getPath(String path, User currentUser) {
        Path current = pathFactory.getRoot();
        String[] elements = path.split("/");
        for (int index = 0; index < elements.length; index++) {
            String element = elements[index];
            if (element.isEmpty()) {
                continue;
            }
            current = pathFactory.resolve(current, elements[index], currentUser);
            if (current.isError()) {
                return current;
            }
            if ((index < elements.length - 1) && !current.isItemFolder()) {
                return Path.error(new Error(current.getItem()));
            }
        }
        return current;
    }

    public Output processCommand(CommandType type, User currentUser, Input input) {
        Command<?> command = commandFactory.createCommand(type, input);
        return command.canExecute(currentUser) ?
                new Output(command.execute(currentUser)) :
                new Output(command.handleError(currentUser));
    }
}
