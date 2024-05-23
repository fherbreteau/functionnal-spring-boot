package io.github.fherbreteau.functional.domain.access.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightParser;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FullAccessParserTest {

    @Test
    void shouldReturnNullIfAccessIsInvalid() {
        AccessRightParser parser = new FullAccessParser(null, null, "", null);
        assertThat(parser.resolve(ItemInput.builder(null), null)).isNull();
    }
}
