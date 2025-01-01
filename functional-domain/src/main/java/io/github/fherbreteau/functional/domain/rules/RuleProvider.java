package io.github.fherbreteau.functional.domain.rules;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.Objects;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.driven.rules.RuleLoader;

public class RuleProvider {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    private final RuleLoader ruleLoader;
    private final String expectedRules;

    public RuleProvider(RuleLoader ruleLoader, String expectedRules) {
        this.ruleLoader = ruleLoader;
        this.expectedRules = expectedRules;
    }

    public void defineRules() {
        String existingRules = ruleLoader.readRules();
        debug(logger, "Existing rules '{0}'", summarize(existingRules));

        if (!Objects.equals(existingRules, expectedRules)) {
            debug(logger, "Existing rules '{0}'", summarize(expectedRules));
            ruleLoader.writeRules(expectedRules);
        }
    }

    private String summarize(String rules) {
        if (Objects.isNull(rules)) {
            return "No rules defined";
        }
        StringTokenizer tokenizer = new StringTokenizer(rules);
        String start = "";
        String end = "";
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (start.isEmpty()) {
                start = token;
            }
            end = token;
        }

        return String.format("%s ... %s", start, end);
    }
}
