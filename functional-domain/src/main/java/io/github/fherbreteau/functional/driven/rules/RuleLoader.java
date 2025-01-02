package io.github.fherbreteau.functional.driven.rules;

import io.github.fherbreteau.functional.domain.entities.Rules;

public interface RuleLoader {

    Rules readRules();

    void writeRules(Rules rules);
}
