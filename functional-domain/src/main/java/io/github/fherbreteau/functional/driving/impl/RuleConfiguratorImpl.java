package io.github.fherbreteau.functional.driving.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.rules.RuleProvider;
import io.github.fherbreteau.functional.driving.RuleConfigurator;

public class RuleConfiguratorImpl implements RuleConfigurator {

    private final Logger logger = Logger.getLogger(RuleConfigurator.class.getSimpleName());
    private final RuleProvider provider;

    public RuleConfiguratorImpl(RuleProvider provider) {
        this.provider = provider;
    }

    @Override
    public void defineRules() {
        debug(logger, "Define rules");
        provider.defineRules();
    }
}
