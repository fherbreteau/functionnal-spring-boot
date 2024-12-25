package io.github.fherbreteau.functional.domain.path.impl;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.PathParser;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimplePathParserTest {

    @Mock
    private ItemRepository repository;

    @Mock
    private AccessChecker accessChecker;

    @Mock
    private User actor;

    @Test
    void testParserReturnsAnErrorWhenCurrentItemIsAFile() {
        // GIVEN
        File file = File.builder()
                .withHandle(UUID.randomUUID())
                .withOwner(User.root())
                .withGroup(Group.root())
                .withName("file")
                .build();
        Path path = Path.success(file);
        String segment = "unused";
        PathParser parser = new SimplePathParser(repository, accessChecker, path, segment);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
        verify(accessChecker, never()).canExecute(any(), eq(actor));
        verify(repository, never()).findByNameAndParentAndUser(any(), any(), eq(actor));
    }

    @Test
    void testParserReturnsAnErrorWhenCurrentItemIsAFolderNotExecutable() {
        // GIVEN
        Folder folder = Folder.builder()
                .withHandle(UUID.randomUUID())
                .withOwner(User.root())
                .withGroup(Group.root())
                .withName("folder")
                .build();
        Path path = Path.success(folder);
        String segment = "unused";
        PathParser parser = new SimplePathParser(repository, accessChecker, path, segment);
        given(accessChecker.canExecute(path.getItem(), actor)).willReturn(false);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
        verify(accessChecker).canExecute(path.getItem(), actor);
        verify(repository, never()).findByNameAndParentAndUser(any(), any(), eq(actor));
    }

    @Test
    void testParserReturnsAnErrorWhenCurrentFolderDoesNotContainGivenItem() {
        // GIVEN
        Folder folder = Folder.builder()
                .withHandle(UUID.randomUUID())
                .withOwner(User.root())
                .withGroup(Group.root())
                .withName("folder")
                .build();
        Path path = Path.success(folder);
        String segment = "notFound";
        PathParser parser = new SimplePathParser(repository, accessChecker, path, segment);
        given(accessChecker.canExecute(path.getItem(), actor)).willReturn(true);
        given(repository.findByNameAndParentAndUser(segment, path.getAsFolder(), actor))
                .willReturn(null);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
        verify(accessChecker).canExecute(path.getItem(), actor);
        verify(repository).findByNameAndParentAndUser(segment, path.getAsFolder(), actor);
    }

    @Test
    void testParserReturnsASuccessWhenCurrentFolderContainGivenItem() {
        // GIVEN
        Folder folder = Folder.builder()
                .withHandle(UUID.randomUUID())
                .withOwner(User.root())
                .withGroup(Group.root())
                .withName("folder")
                .build();
        Path path = Path.success(folder);
        String segment = "file";
        PathParser parser = new SimplePathParser(repository, accessChecker, path, segment);
        given(accessChecker.canExecute(path.getItem(), actor)).willReturn(true);
        File result = File.builder()
                .withOwner(User.root())
                .withGroup(Group.root())
                .withName(segment)
                .withParent(path.getAsFolder())
                .build();
        given(repository.findByNameAndParentAndUser(segment, path.getAsFolder(), actor))
                .willReturn(of(result));
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isFalse();
        assertThat(resolved).extracting(Path::isItemFile, BOOLEAN).isTrue();
        verify(accessChecker).canExecute(path.getItem(), actor);
        verify(repository).findByNameAndParentAndUser(segment, path.getAsFolder(), actor);
    }
}
