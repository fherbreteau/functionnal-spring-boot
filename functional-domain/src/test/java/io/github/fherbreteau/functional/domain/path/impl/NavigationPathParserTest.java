package io.github.fherbreteau.functional.domain.path.impl;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.path.PathParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;

@ExtendWith(MockitoExtension.class)
class NavigationPathParserTest {

    @Mock
    private User actor;

    @Test
    void testParserReturnsAnErrorWhenPathStayInSamePlaceAndItemIsFile() {
        // GIVEN
        Path path = Path.success(File.builder().withName("file").build());
        String segment = ".";
        PathParser parser = new NavigationPathParser(path, segment, UnaryOperator.identity());
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
    }

    @Test
    void testParserReturnsAnErrorWhenFunctionReturnsNoItem() {
        // GIVEN
        Path path = Path.success(Folder.builder().withName("file").build());
        String segment = ".";
        PathParser parser = new NavigationPathParser(path, segment, i -> null);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
    }

    @Test
    void testParserReturnsASuccessWhenFunctionReturnsAnItem() {
        // GIVEN
        Folder folder = Folder.builder().withName("folder").withParent(Folder.getRoot()).build();
        Path path = Path.success(File.builder().withName("file").withParent(folder).build());
        String segment = "..";
        PathParser parser = new NavigationPathParser(path, segment, Item::getParent);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isFalse();
        assertThat(resolved).extracting(Path::isItemFolder, BOOLEAN).isTrue();
        assertThat(resolved).extracting(Path::getItem).isEqualTo(folder);
    }
}
