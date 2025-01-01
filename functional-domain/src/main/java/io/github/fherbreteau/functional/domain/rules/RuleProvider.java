package io.github.fherbreteau.functional.domain.rules;

import java.util.Objects;

import io.github.fherbreteau.functional.domain.entities.Rules;
import io.github.fherbreteau.functional.driven.rules.RuleLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private final RuleLoader ruleLoader;
    private final Rules rules;

    public RuleProvider(RuleLoader ruleLoader, Rules rules) {
        this.ruleLoader = ruleLoader;
        this.rules = rules;
    }

    public void defineRules() {
        Rules existingRules = ruleLoader.readRules();
        logger.debug("Existing rules '{}'", existingRules);

        if (!Objects.equals(rules, existingRules)) {
            logger.debug("Existing rules '{}'", rules);
            ruleLoader.writeRules(rules);
        }
    }
}
