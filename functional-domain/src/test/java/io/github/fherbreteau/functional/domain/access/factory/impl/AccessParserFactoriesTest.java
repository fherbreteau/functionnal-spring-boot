package io.github.fherbreteau.functional.domain.access.factory.impl;

import static io.github.fherbreteau.functional.domain.access.AccessParser.STEP_ACTION;
import static io.github.fherbreteau.functional.domain.access.AccessParser.STEP_ATTRIBUTION;
import static io.github.fherbreteau.functional.domain.access.AccessParser.STEP_RIGHT;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.fherbreteau.functional.domain.access.AccessContext;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import org.junit.jupiter.api.Test;

class AccessParserFactoriesTest {

    private final AccessContext context = new AccessContext();

    @Test
    void testThatNullItemIsCorrectlyChecked() {
        context.setStep(STEP_ATTRIBUTION);
        AccessParserFactory factory = new AttributionAccessParserAccessFactory();
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
        factory = new RightAccessParserAccessFactory();
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
