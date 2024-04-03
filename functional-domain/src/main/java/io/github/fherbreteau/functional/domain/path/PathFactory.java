package io.github.fherbreteau.functional.domain.path;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class PathFactory {

    private final FileRepository repository;

    private final AccessChecker accessChecker;

    public PathFactory(FileRepository repository, AccessChecker accessChecker) {
        this.repository = repository;
        this.accessChecker = accessChecker;
    }

    public Path resolve(Path current, String segment, User currentUser) {
        if (! accessChecker.canExecute(current.getItem(), currentUser))
            return Path.error(new Error(current.getItem(), currentUser));
        return Path.success(segment, repository.findByNameAndParentAndUser(segment, current.getItemAsFolder(), currentUser));
    }

    public Path getRoot() {
        return Path.ROOT;
    }
}
