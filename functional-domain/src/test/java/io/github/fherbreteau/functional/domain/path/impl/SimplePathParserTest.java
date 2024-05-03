package io.github.fherbreteau.functional.domain.path.impl;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SimplePathParserTest {

    @Mock
    private FileRepository repository;

    @Mock
    private AccessChecker accessChecker;

    @Mock
    private User actor;

    @Test
    void testParserReturnsAnErrorWhenCurrentItemIsAFile() {
        // GIVEN
        Path path = Path.success(File.builder().withName("file").build());
        String segment = "unused";
        PathParser parser = new SimplePathParser(repository, accessChecker, path, segment);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
    }

    @Test
    void testParserReturnsAnErrorWhenCurrentItemIsAFolderNotExecutable() {
        // GIVEN
        Path path = Path.success(Folder.builder().withName("folder").build());
        String segment = "unused";
        PathParser parser = new SimplePathParser(repository, accessChecker, path, segment);
        given(accessChecker.canExecute(path.getItem(), actor)).willReturn(false);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
    }

    @Test
    void testParserReturnsAnErrorWhenCurrentFolderDoesNotContainGivenItem() {
        // GIVEN
        Path path = Path.success(Folder.builder().withName("folder").build());
        String segment = "notFound";
        PathParser parser = new SimplePathParser(repository, accessChecker, path, segment);
        given(accessChecker.canExecute(path.getItem(), actor)).willReturn(true);
        given(repository.findByNameAndParentAndUser(segment, path.getAsFolder(), actor))
                .willReturn(null);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
    }

    @Test
    void testParserReturnsASuccessWhenCurrentFolderContainGivenItem() {
        // GIVEN
        Path path = Path.success(Folder.builder().withName("folder").build());
        String segment = "file";
        PathParser parser = new SimplePathParser(repository, accessChecker, path, segment);
        given(accessChecker.canExecute(path.getItem(), actor)).willReturn(true);
        given(repository.findByNameAndParentAndUser(segment, path.getAsFolder(), actor))
                .willReturn(File.builder().withName(segment).withParent(path.getAsFolder()).build());
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isFalse();
        assertThat(resolved).extracting(Path::isItemFile, BOOLEAN).isTrue();
    }
}
