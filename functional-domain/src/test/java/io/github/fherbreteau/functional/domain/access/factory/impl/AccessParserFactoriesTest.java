package io.github.fherbreteau.functional.domain.access.factory.impl;

import io.github.fherbreteau.functional.domain.access.AccessRightContext;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import org.junit.jupiter.api.Test;

import static io.github.fherbreteau.functional.domain.access.AccessRightParser.*;
import static org.assertj.core.api.Assertions.assertThat;

class AccessParserFactoriesTest {

    private final AccessRightContext context = new AccessRightContext();

    @Test
    void testThatNullItemIsCorrectlyChecked() {
        context.setStep(STEP_ATTRIBUTION);
        AccessParserFactory factory = new AttributionAccessParserFactory();
        assertThat(factory.supports(context, "og", null)).isFalse();
        factory = new EveryoneAccessParserFactory();
        assertThat(factory.supports(context, "a", null)).isFalse();
        factory = new GroupAccessParserFactory();
        assertThat(factory.supports(context, "g", null)).isFalse();
        factory = new OtherAccessParserFactory();
        assertThat(factory.supports(context, "o", null)).isFalse();
        factory = new OwnerAccessParserFactory();
        assertThat(factory.supports(context, "u", null)).isFalse();

        context.setStep(STEP_ACTION);
        factory = new AddAccessParserFactory();
        assertThat(factory.supports(context, "+", null)).isFalse();
        factory = new RemoveAccessParserFactory();
        assertThat(factory.supports(context, "-", null)).isFalse();
        factory = new SetAccessParserFactory();
        assertThat(factory.supports(context, "=", null)).isFalse();

        context.setStep(STEP_RIGHT);
        factory = new ExecuteAccessParserFactory();
        assertThat(factory.supports(context, "x", null)).isFalse();
        factory = new ReadAccessParserFactory();
        assertThat(factory.supports(context, "r", null)).isFalse();
        factory = new RightAccessParserFactory();
        assertThat(factory.supports(context, "rwx", null)).isFalse();
        factory = new WriteAccessParserFactory();
        assertThat(factory.supports(context, "w", null)).isFalse();
    }

    @Test
    void testThatNoMatchingIsCorrectlyChecked() {
        File file = File.builder()
                .withName("")
                .withOwner(User.root())
                .withGroup(Group.root())
                .build();
        context.setStep(STEP_ATTRIBUTION);
        AccessParserFactory factory = new OwnerAccessParserFactory();
        assertThat(factory.supports(context, "o", file)).isFalse();

        context.setStep(STEP_RIGHT);
        factory = new WriteAccessParserFactory();
        assertThat(factory.supports(context, "r", file)).isFalse();

    }
}
