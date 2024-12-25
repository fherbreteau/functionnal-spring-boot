package io.github.fherbreteau.functional.domain.path.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;

import java.util.function.UnaryOperator;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NavigationPathParserTest {

    @Mock
    private User actor;

    @Test
    void testParserReturnsAnErrorWhenPathStayInSamePlaceAndItemIsFile() {
        // GIVEN
        Path path = Path.success(File.builder()
                .withOwner(User.root())
                .withGroup(Group.root())
                .withName("file")
                .build());
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
        Path path = Path.success(Folder.builder()
                .withOwner(User.root())
                .withGroup(Group.root())
                .withName("file")
                .build());
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
        Folder folder = Folder.builder()
                .withOwner(User.root())
                .withGroup(Group.root())
                .withName("folder")
                .withParent(Folder.getRoot())
                .build();
        Path path = Path.success(File.builder()
                .withOwner(User.root())
                .withGroup(Group.root())
                .withName("file")
                .withParent(folder)
                .build());
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
