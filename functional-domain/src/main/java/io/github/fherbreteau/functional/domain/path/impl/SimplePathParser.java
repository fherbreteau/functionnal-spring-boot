package io.github.fherbreteau.functional.domain.path.impl;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.Optional;

public class SimplePathParser implements PathParser {

    private final FileRepository repository;

    private final AccessChecker accessChecker;

    private final Path current;

    private final String segment;

    public SimplePathParser(FileRepository repository, AccessChecker accessChecker, Path current, String segment) {
        this.repository = repository;
        this.accessChecker = accessChecker;
        this.current = current;
        this.segment = segment;
    }

    @Override
    public Path resolve(User actor) {
        return Optional.of(current)
                .filter(Path::isItemFolder)
                .map(Path::getAsFolder)
                .filter(i -> accessChecker.canExecute(i, actor))
                .map(i -> repository.findByNameAndParentAndUser(segment, i, actor))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Item.class::cast)
                .map(Path::success)
                .orElseGet(() -> Path.error(Error.error(String.format("%s not found in %s for %s", segment, current.getItem(), actor))));
    }
}
