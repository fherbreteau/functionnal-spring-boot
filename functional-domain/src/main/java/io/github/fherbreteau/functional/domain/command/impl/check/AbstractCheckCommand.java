package io.github.fherbreteau.functional.domain.command.impl.check;

import java.util.List;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCheckCommand<T, C extends Command<Output<T>>, E extends Command<Output<T>>> implements CheckCommand<T> {
    protected final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public final Command<Output<T>> execute(User actor) {
        List<String> reasons = checkAccess(actor);
        if (!reasons.isEmpty()) {
            logger.debug("Command will fail for {} because of {}", actor, reasons);
            return createError(reasons);
        }
        logger.debug("Command will continue for {}", actor);
        return createSuccess();
    }

    protected abstract List<String> checkAccess(User actor);

    protected abstract C createSuccess();

    protected abstract E createError(List<String> reasons);
}
