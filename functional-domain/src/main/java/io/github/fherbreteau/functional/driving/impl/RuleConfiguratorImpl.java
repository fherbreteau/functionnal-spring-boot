package io.github.fherbreteau.functional.driving.impl;

import io.github.fherbreteau.functional.domain.rules.RuleProvider;
import io.github.fherbreteau.functional.driving.RuleConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleConfiguratorImpl implements RuleConfigurator {

    private final Logger logger = LoggerFactory.getLogger(RuleConfigurator.class.getSimpleName());
    private final RuleProvider provider;

    public RuleConfiguratorImpl(RuleProvider provider) {
        this.provider = provider;
    }

    @Override
    public void defineRules() {
        logger.debug("Define checking rules");
        provider.defineRules();
    }
}
