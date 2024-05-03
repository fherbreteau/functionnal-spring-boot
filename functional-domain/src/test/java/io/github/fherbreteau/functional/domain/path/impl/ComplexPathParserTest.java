package io.github.fherbreteau.functional.domain.path.impl;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.PathParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ComplexPathParserTest {

    @Mock
    private CompositePathFactory compositePathFactory;

    @Mock
    private User actor;

    @Test
    void testParserReturnsAnErrorWhenCurrentElementParserReturnsAnError() {
        // GIVEN
        Path path = Path.ROOT;
        String segment = "file/folder";
        PathParser parser = new ComplexPathParser(compositePathFactory, path, segment);
        given(compositePathFactory.createParser(path, "file")).willReturn(new InvalidPathParser(path, segment));
        given(compositePathFactory.createParser(any(), eq("folder")))
                .willAnswer(invocation -> new InvalidPathParser(invocation.getArgument(0), segment));
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
    }

    @Test
    void testParserReturnAnErrorWhenRestElementParserReturnsAnError() {
        // GIVEN
        Path path = Path.ROOT;
        String segment = "file/folder";
        PathParser parser = new ComplexPathParser(compositePathFactory, path, segment);
        PathParser elementParser = mock(PathParser.class);
        given(compositePathFactory.createParser(path, "file")).willReturn(elementParser);
        Path partial = Path.success(File.builder().withName("file").withParent(Folder.getRoot()).build());
        given(elementParser.resolve(actor)).willReturn(partial);
        given(compositePathFactory.createParser(partial, "folder"))
                .willReturn(new InvalidPathParser(partial, "folder"));
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isTrue();
    }

    @Test
    void testParserReturnASuccessWhenAllInternalParserReturnsASuccess() {
        // GIVEN
        Path path = Path.ROOT;
        String segment = "folder/file";
        PathParser parser = new ComplexPathParser(compositePathFactory, path, segment);

        PathParser elementParser = mock(PathParser.class);
        given(compositePathFactory.createParser(path, "folder")).willReturn(elementParser);
        Path partial = Path.success(Folder.builder().withName("folder").withParent(Folder.getRoot()).build());
        given(elementParser.resolve(actor)).willReturn(partial);

        PathParser restParser = mock(PathParser.class);
        given(compositePathFactory.createParser(partial, "file")).willReturn(restParser);
        Path result = Path.success(Folder.builder().withName("file").withParent(partial.getAsFolder()).build());
        given(restParser.resolve(actor)).willReturn(result);
        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isEqualTo(result);
    }
}
