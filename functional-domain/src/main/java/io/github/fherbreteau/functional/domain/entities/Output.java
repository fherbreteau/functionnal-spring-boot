package io.github.fherbreteau.functional.domain.entities;

public class Output {

    private final Object value;

    private final Error error;

    public Output(Object value) {
        this(value, null);
    }

    public Output(Error error) {
        this(null, error);
    }

    private Output(Object value, Error error) {
        this.value = value;
        this.error = error;
    }

    public Object getValue() {
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
