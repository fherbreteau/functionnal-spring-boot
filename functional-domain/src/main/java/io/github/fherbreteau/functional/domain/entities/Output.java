package io.github.fherbreteau.functional.domain.entities;

import java.util.List;

public final class Output<T> {

    private final T value;

    private final Error error;

    private Output(T value, Error error) {
        this.value = value;
        this.error = error;
    }

    public T getValue() {
        return value;
    }

    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isError() {
        return error != null;
    }

    public static <T> Output<T> success(T value) {
        return new Output<>(value, null);
    }

    public static <T> Output<T> error(Throwable throwable) {
        return error(throwable.getMessage());
    }

    public static <T> Output<T> error(String message) {
        return error(message, List.of());
    }

    public static <T> Output<T> error(String message, List<String> reasons) {
        return new Output<>(null, Error.error(message, reasons));
    }

    @Override
    public String toString() {
        String result = "Output{";
        if (value != null) {
            result += "value=" + value;
        } else {
            result += "error=" + error;
        }
        return result + '}';
    }
}
