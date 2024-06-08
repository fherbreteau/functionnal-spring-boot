package io.github.fherbreteau.functional.domain.path.impl;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;

@ExtendWith(MockitoExtension.class)
class InvalidPathParserTest {

    @Mock
    private User actor;

    @Test
    void testReturnsAnErrorInAnyCase() {
        // GIVEN
        Path path = Path.success(File.builder()
                .withName("file")
                .withOwner(User.root())
                .withGroup(Group.root())
                .build());
        String segment = ".";
        PathParser parser = new InvalidPathParser(path, segment);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
    }
}
