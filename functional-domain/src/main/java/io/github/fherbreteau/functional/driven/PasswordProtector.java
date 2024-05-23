package io.github.fherbreteau.functional.driven;

import java.util.List;

public interface PasswordProtector {

    String protect(String rawPassword);

    List<String> validate(String rawPassword);
}
