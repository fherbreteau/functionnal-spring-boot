package io.github.fherbreteau.functional.domain.path.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.CompositePathParserFactory;
import io.github.fherbreteau.functional.domain.path.PathParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ComplexPathParserTest {

    @Mock
    private CompositePathParserFactory compositePathParserFactory;

    @Mock
    private User actor;

    @Captor
    private ArgumentCaptor<Path> pathCaptor;

    @Test
    void testParserReturnsAnErrorWhenCurrentElementParserReturnsAnError() {
        // GIVEN
        Path path = Path.ROOT;
        String segment = "file/folder";
        PathParser parser = new ComplexPathParser(compositePathParserFactory, path, segment);
        given(compositePathParserFactory.createParser(path, "file")).willReturn(new InvalidPathParser(path, "file"));
        given(compositePathParserFactory.createParser(any(), eq("folder")))
                .willAnswer(invocation -> new InvalidPathParser(invocation.getArgument(0), "folder"));
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
        verify(compositePathParserFactory).createParser(path, "file");
        verify(compositePathParserFactory).createParser(pathCaptor.capture(), eq("folder"));
        assertThat(pathCaptor.getValue()).isNotNull()
                .extracting(Path::isError, BOOLEAN).isTrue();
    }

    @Test
    void testParserReturnAnErrorWhenRestElementParserReturnsAnError() {
        // GIVEN
        Path path = Path.ROOT;
        String segment = "file/folder";
        PathParser parser = new ComplexPathParser(compositePathParserFactory, path, segment);
        PathParser elementParser = mock(PathParser.class);
        given(compositePathParserFactory.createParser(path, "file")).willReturn(elementParser);
        Path partial = Path.success(File.builder()
                .withName("file")
                .withOwner(User.root())
                .withGroup(Group.root())
                .withParent(Folder.getRoot())
                .build());
        given(elementParser.resolve(actor)).willReturn(partial);
        given(compositePathParserFactory.createParser(partial, "folder"))
                .willReturn(new InvalidPathParser(partial, "folder"));
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
        verify(compositePathParserFactory).createParser(path, "file");
        verify(compositePathParserFactory).createParser(partial, "folder");
    }

    @Test
    void testParserReturnASuccessWhenAllInternalParserReturnsASuccess() {
        // GIVEN
        Path path = Path.ROOT;
        String segment = "folder/file";
        PathParser parser = new ComplexPathParser(compositePathParserFactory, path, segment);

        PathParser elementParser = mock(PathParser.class);
        given(compositePathParserFactory.createParser(path, "folder")).willReturn(elementParser);
        Path partial = Path.success(Folder.builder()
                .withName("folder")
                .withOwner(User.root())
                .withGroup(Group.root())
                .withParent(Folder.getRoot())
                .build());
        given(elementParser.resolve(actor)).willReturn(partial);

        PathParser restParser = mock(PathParser.class);
        given(compositePathParserFactory.createParser(partial, "file")).willReturn(restParser);
        Path result = Path.success(Folder.builder()
                .withName("file")
                .withOwner(User.root())
                .withGroup(Group.root())
                .withParent(partial.getAsFolder())
                .build());
        given(restParser.resolve(actor)).willReturn(result);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isEqualTo(result);
        verify(compositePathParserFactory).createParser(path, "folder");
        verify(compositePathParserFactory).createParser(partial, "file");
    }

    @Test
    void testParserReturnASuccessWhenPathEndsWithAPathSeparator() {
        // GIVEN
        Path path = Path.ROOT;
        String segment = "folder/";
        PathParser parser = new ComplexPathParser(compositePathParserFactory, path, segment);

        PathParser elementParser = mock(PathParser.class);
        given(compositePathParserFactory.createParser(path, "folder")).willReturn(elementParser);
        Path partial = Path.success(Folder.builder()
                .withName("folder")
                .withOwner(User.root())
                .withGroup(Group.root())
                .withParent(Folder.getRoot())
                .build());
        given(elementParser.resolve(actor)).willReturn(partial);

        PathParser restParser = mock(PathParser.class);
        given(compositePathParserFactory.createParser(partial, "")).willReturn(restParser);
        given(restParser.resolve(actor)).willReturn(partial);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isEqualTo(partial);
        verify(compositePathParserFactory).createParser(path, "folder");
        verify(compositePathParserFactory).createParser(partial, "");
    }
}
