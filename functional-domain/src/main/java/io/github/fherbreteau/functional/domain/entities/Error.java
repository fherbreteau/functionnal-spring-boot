package io.github.fherbreteau.functional.domain.entities;

public class Error {

    private final String message;

    public Error(CommandType type, Input input, User actor) {
        this(String.format("%s with arguments %s failed for %s", type, input, actor));
    }

    public Error(Item item, String segment, User actor) {
        this(String.format("%s not found in %s for %s", segment, item, actor));
    }

    public Error(Item item, User actor) {
        this(String.format("%s is not executable for %s", item, actor));
    }

    public Error(Item item) {
        this(String.format("%s is not a folder", item));
    }

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
