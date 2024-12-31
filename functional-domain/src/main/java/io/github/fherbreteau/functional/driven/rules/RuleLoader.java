package io.github.fherbreteau.functional.driven.rules;

public interface RuleLoader {

    String readRules();

    void writeRules(String rules);
}
