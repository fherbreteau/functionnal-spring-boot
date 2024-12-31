package io.github.fherbreteau.functional.domain;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Logging {

    private Logging() { }

    public static void debug(Logger logger, String message, Object... params) {
        logger.log(Level.FINE, message, params);
    }

    public static void error(Logger logger, String message, Object... params) {
        logger.log(Level.SEVERE, message, params);
    }
}
