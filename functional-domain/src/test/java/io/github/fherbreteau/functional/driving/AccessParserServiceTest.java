package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.*;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.github.fherbreteau.functional.domain.entities.AccessRight.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccessParserServiceTest {

    private AccessParserService accessParserService;

    public static Stream<Arguments> shouldMapAnAccessStringToRequiredAccessRight() {
        File fileNoAccess = File.builder().build();
        File fileFullAccess = fileNoAccess.copyBuilder()
                .withOwnerAccess(full())
                .withGroupAccess(full())
                .withOtherAccess(full())
                .build();
        return Stream.of(
                Arguments.of("+r", fileNoAccess, readOnly(), null, null),
                Arguments.of("+w", fileNoAccess, writeOnly(), null, null),
                Arguments.of("+x", fileNoAccess, executeOnly(), null, null),
                Arguments.of("-r", fileFullAccess, writeExecute(), null, null),
                Arguments.of("-w", fileFullAccess, readExecute(), null, null),
                Arguments.of("-x", fileFullAccess, readWrite(), null, null),
                Arguments.of("=r", fileNoAccess, readOnly(), null, null),
                Arguments.of("=w", fileNoAccess, writeOnly(), null, null),
                Arguments.of("=x", fileNoAccess, executeOnly(), null, null),
                Arguments.of("g+r", fileNoAccess, null, readOnly(), null),
                Arguments.of("g+w", fileNoAccess, null, writeOnly(), null),
                Arguments.of("g+x", fileNoAccess, null, executeOnly(), null),
                Arguments.of("g-r", fileFullAccess, null, writeExecute(), null),
                Arguments.of("g-w", fileFullAccess, null, readExecute(), null),
                Arguments.of("g-x", fileFullAccess, null, readWrite(), null),
                Arguments.of("g=r", fileNoAccess, null, readOnly(), null),
                Arguments.of("g=w", fileNoAccess, null, writeOnly(), null),
                Arguments.of("g=x", fileNoAccess, null, executeOnly(), null),
                Arguments.of("o+r", fileNoAccess, null, null, readOnly()),
                Arguments.of("o+w", fileNoAccess, null, null, writeOnly()),
                Arguments.of("o+x", fileNoAccess, null, null, executeOnly()),
                Arguments.of("o-r", fileFullAccess, null, null, writeExecute()),
                Arguments.of("o-w", fileFullAccess, null, null, readExecute()),
                Arguments.of("o-x", fileFullAccess, null, null, readWrite()),
                Arguments.of("o=r", fileNoAccess, null, null, readOnly()),
                Arguments.of("o=w", fileNoAccess, null, null, writeOnly()),
                Arguments.of("o=x", fileNoAccess, null, null, executeOnly()),
                Arguments.of("a+r", fileNoAccess, readOnly(), readOnly(), readOnly()),
                Arguments.of("a+w", fileNoAccess, writeOnly(), writeOnly(), writeOnly()),
                Arguments.of("a+x", fileNoAccess, executeOnly(), executeOnly(), executeOnly()),
                Arguments.of("a-r", fileFullAccess, writeExecute(), writeExecute(), writeExecute()),
                Arguments.of("a-w", fileFullAccess, readExecute(), readExecute(), readExecute()),
                Arguments.of("a-x", fileFullAccess, readWrite(), readWrite(), readWrite()),
                Arguments.of("a=r", fileNoAccess, readOnly(), readOnly(), readOnly()),
                Arguments.of("a=w", fileNoAccess, writeOnly(), writeOnly(), writeOnly()),
                Arguments.of("a=x", fileNoAccess, executeOnly(), executeOnly(), executeOnly()),
                Arguments.of("ugo+rwx", fileNoAccess, full(), full(), full()),
                Arguments.of("+rwx", fileNoAccess, full(), null, null),
                Arguments.of("o-rwx", fileFullAccess, null, null, none()),
                Arguments.of("g+x", fileFullAccess, null, full(), null),
                Arguments.of("uo-wx", fileFullAccess, readOnly(), null, readOnly()),
                Arguments.of("go-w", fileFullAccess, null, readExecute(), readExecute())
        );
    }

    public static Stream<Arguments> shouldThrowAnIllegalStateException() {
        return Stream.of(
                Arguments.of(null, File.builder().build()),
                Arguments.of("", File.builder().build()),
                Arguments.of("a", File.builder().build()),
                Arguments.of("g", File.builder().build()),
                Arguments.of("r", File.builder().build()),
                Arguments.of("u", File.builder().build()),
                Arguments.of("w", File.builder().build()),
                Arguments.of("x", File.builder().build()),
                Arguments.of("+rwx", null),
                Arguments.of("l", File.builder().build())
                );
    }

    @BeforeEach
    public void setup() {
        List<AccessParserFactory> accessRightParserFactories = List.of(
                new AddAccessParserFactory(),
                new AttributionAccessParserFactory(),
                new EveryoneAccessParserFactory(),
                new UnsupportedAccessParserFactory(),
                new ExecuteAccessParserFactory(),
                new FullAccessParserFactory(),
                new GroupAccessParserFactory(),
                new OtherAccessParserFactory(),
                new OwnerAccessParserFactory(),
                new ReadAccessParserFactory(),
                new RemoveAccessParserFactory(),
                new RightAccessParserFactory(),
                new SetAccessParserFactory(),
                new WriteAccessParserFactory()
        );
        CompositeAccessParserFactory accessRightParserFactory = new CompositeAccessParserFactory(accessRightParserFactories);
        accessRightParserFactory.configureRecursive();
        accessParserService = new AccessParserService(accessRightParserFactory);
    }

    @ParameterizedTest(name = "{0} should be parsed to {2}{3}{4}")
    @MethodSource
    void shouldMapAnAccessStringToRequiredAccessRight(String right, Item itemToUpdate, AccessRight owner, AccessRight group, AccessRight other) {
        Input input = accessParserService.parseAccessRights(right, itemToUpdate);
        assertThat(input.getOwnerAccess()).isEqualTo(owner);
        assertThat(input.getGroupAccess()).isEqualTo(group);
        assertThat(input.getOtherAccess()).isEqualTo(other);
    }

    @ParameterizedTest(name = "{0} should not be parsable")
    @MethodSource
    void shouldThrowAnIllegalStateException(String right, Item itemToUpdate) {
        assertThatThrownBy(() -> accessParserService.parseAccessRights(right, itemToUpdate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Access right is not valid");
    }
}
