package io.github.fherbreteau.functional.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RulesTest {

    @Test
    void shouldSummarizeTheRulesWithToStrong() {
        assertThat(new Rules("""
            group { }

            user { }
            item { }
            """)).hasToString("group { } ... item { }");
    }
}
