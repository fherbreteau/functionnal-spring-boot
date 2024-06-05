package io.github.fherbreteau.functional.domain.path;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.domain.path.factory.impl.*;
import io.github.fherbreteau.functional.domain.path.impl.InvalidPathParser;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CompositePathFactoryTest {
    private CompositePathFactory compositePathFactory;
    @Mock
    private ItemRepository repository;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private User actor;

    public static Stream<Arguments> validPathArguments() {
        Path folder = Path.success(Folder.builder().withName("folder").withParent(Folder.getRoot()).build());
        Path file = Path.success(File.builder().withName("file").withParent(Folder.getRoot()).build());
        return Stream.of(
                // Element checker
                Arguments.of(Path.ROOT, "file"),
                Arguments.of(Path.ROOT, "folder"),
                Arguments.of(Path.ROOT, "."),
                Arguments.of(folder, ".."),
                Arguments.of(folder, "."),
                // Start checker
                Arguments.of(Path.ROOT, "/file"),
                Arguments.of(Path.ROOT, "/folder"),
                // Sub Element
                Arguments.of(Path.ROOT, "folder/file"),
                Arguments.of(file, "../folder")
        );
    }

    public static Stream<Arguments> invalidPathArguments() {
        Path folder = Path.success(Folder.builder().withName("folder").withParent(Folder.getRoot()).build());
        Path file = Path.success(File.builder().withName("file").withParent(folder.getAsFolder()).build());
        Path error = Path.error(Error.error("error"));
        return Stream.of(
                // Go up
                Arguments.of(Path.ROOT, ".."),
                // Element after file
                Arguments.of(file, "folder"),
                // Element after error
                Arguments.of(error, ".")
        );
    }

    @BeforeEach
    public void setup() {
        List<PathFactory> pathFactories = List.of(
                new ParentSegmentPathFactory(),
                new CurrentSegmentPathFactory(),
                new EmptySegmentPathFactory(),
                new SingleSegmentPathFactory(),
                new ComplexSegmentPathFactory(),
                new InvalidPathFactory()
        );
        compositePathFactory = new CompositePathFactory(repository, accessChecker, pathFactories);
        compositePathFactory.configureRecursive();
    }

    @ParameterizedTest
    @MethodSource("validPathArguments")
    void testParserCreatedForValidPathAndRoot(Path currentPath, String path) {
        // WHEN
        PathParser parser = compositePathFactory.createParser(currentPath, path);
        // THEN
        assertThat(parser).isNotNull().isNotInstanceOf(InvalidPathParser.class);
    }

    @ParameterizedTest
    @MethodSource("invalidPathArguments")
    void testParserCreatedForInvalidPathAndRoot(Path currentPath, String path) {
        // WHEN
        PathParser parser = compositePathFactory.createParser(currentPath, path);
        // THEN
        assertThat(parser).isNotNull().isInstanceOf(InvalidPathParser.class);
    }

    @Test
    void testCreatedParserWillResolveAComplexPath() {
        // GIVEN
        given(accessChecker.canExecute(any(), eq(actor))).willReturn(true);
        File file1 = File.builder().withName("file").withParent(Folder.getRoot()).build();
        given(repository.findByNameAndParentAndUser("file", Folder.getRoot(), actor)).willReturn(of(file1));
        Folder folder = Folder.builder().withName("folder").withParent(Folder.getRoot()).build();
        given(repository.findByNameAndParentAndUser("folder", Folder.getRoot(), actor)).willReturn(of(folder));
        File file2 = File.builder().withName("file").withParent(folder).build();
        given(repository.findByNameAndParentAndUser("file", folder, actor)).willReturn(of(file2));
        // WHEN
        PathParser parser = compositePathFactory.createParser(Path.ROOT, "/file/../folder/./file");
        // THEN
        assertThat(parser).isNotNull().isNotInstanceOf(InvalidPathParser.class);

        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isFalse();
        assertThat(resolved).extracting(Path::isItemFile, BOOLEAN).isTrue();
        assertThat(resolved).extracting(Path::getItem).isEqualTo(file2);
    }

    @Test
    void testCreatedParserWillResolveAComplexPathWithFolder() {
        // GIVEN
        given(accessChecker.canExecute(any(), eq(actor))).willReturn(true);
        File file1 = File.builder().withName("file").withParent(Folder.getRoot()).build();
        given(repository.findByNameAndParentAndUser("file", Folder.getRoot(), actor)).willReturn(of(file1));
        Folder folder1 = Folder.builder().withName("folder").withParent(Folder.getRoot()).build();
        given(repository.findByNameAndParentAndUser("folder", Folder.getRoot(), actor)).willReturn(of(folder1));
        Folder folder2 = Folder.builder().withName("folder").withParent(folder1).build();
        given(repository.findByNameAndParentAndUser("folder", folder1, actor)).willReturn(of(folder2));
        // WHEN
        PathParser parser = compositePathFactory.createParser(Path.ROOT, "/file/../folder/./folder/");
        // THEN
        assertThat(parser).isNotNull().isNotInstanceOf(InvalidPathParser.class);

        // WHEN
        Path resolved = parser.resolve(actor);
        // THEN
        assertThat(resolved).isNotNull().extracting(Path::isError, BOOLEAN).isFalse();
        assertThat(resolved).extracting(Path::isItemFolder, BOOLEAN).isTrue();
        assertThat(resolved).extracting(Path::getItem).isEqualTo(folder2);
    }

    @Test
    void testOrderOfPathFactoriesIsRespected() {
        List<PathFactory> factories = List.of(
                new InvalidPathFactory(),
                new CurrentSegmentPathFactory(),
                new SingleSegmentPathFactory()
        );
        List<PathFactory> sortedFactories = factories.stream().sorted(Comparator.comparing(PathFactory::order)).toList();
        assertThat(sortedFactories).last(type(PathFactory.class))
                .isInstanceOf(InvalidPathFactory.class);

    }
}
