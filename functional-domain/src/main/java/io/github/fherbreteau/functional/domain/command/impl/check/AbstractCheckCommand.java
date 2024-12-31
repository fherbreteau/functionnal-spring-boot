package io.github.fherbreteau.functional.domain.command.impl.check;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.List;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;

public abstract class AbstractCheckCommand<T, C extends Command<Output<T>>, E extends Command<Output<T>>> implements CheckCommand<T> {
    protected final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    public final Command<Output<T>> execute(User actor) {
        List<String> reasons = checkAccess(actor);
        if (!reasons.isEmpty()) {
            debug(logger,  "Command will fail for {0} because of {1}", actor, reasons);
            return createError(reasons);
        }
        debug(logger,  "Command will continue for {0}", actor);
        return createSuccess();
    }

    protected abstract List<String> checkAccess(User actor);

    protected abstract C createSuccess();

    protected abstract E createError(List<String> reasons);
}
