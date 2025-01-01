package io.github.fherbreteau.functional.domain.path.impl;

import java.util.Optional;

import io.github.fherbreteau.functional.domain.entities.Failure;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplePathParser implements PathParser {
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private final ItemRepository repository;
    private final AccessChecker accessChecker;
    private final Path current;
    private final String segment;

    public SimplePathParser(ItemRepository repository, AccessChecker accessChecker, Path current, String segment) {
        this.repository = repository;
        this.accessChecker = accessChecker;
        this.current = current;
        this.segment = segment;
    }

    @Override
    public Path resolve(User actor) {
        logger.debug("Resolving simple path {} with {}", current, segment);
        return Optional.of(current)
                .filter(Path::isItemFolder)
                .map(Path::getAsFolder)
                .filter(i -> accessChecker.canExecute(i, actor))
                .map(i -> repository.findByNameAndParentAndUser(segment, i, actor))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Path::success)
                .orElseGet(() -> Path.error(Failure.failure(String.format("%s not found in %s for %s", segment, current.getItem(), actor))));
    }
}
