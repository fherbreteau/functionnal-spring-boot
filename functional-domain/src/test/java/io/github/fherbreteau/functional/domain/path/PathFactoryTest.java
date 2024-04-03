package io.github.fherbreteau.functional.domain.path;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathFactoryTest {

    private PathFactory pathFactory;
    @Mock
    private FileRepository repository;
    @Mock
    private AccessChecker accessChecker;
    @Mock
    private Folder parent;
    @Mock
    private File item;
    @Mock
    private User actor;
    @Mock
    private Path parentPath;

    @BeforeEach
    public void setup() {
        pathFactory = new PathFactory(repository, accessChecker);
    }

    @Test
    void shouldReturnTheRootPath() {
        assertThat(pathFactory.getRoot()).isNotNull()
                .satisfies(p -> assertThat(p.getName()).isEqualTo(Item.ROOT))
                .satisfies(p -> assertThat(p.getItem()).isEqualTo(Folder.getRoot()))
                .satisfies(p -> assertThat(p.getError()).isNull());
    }

    @Test
    void shouldLocateInRepositoryWhenCurrentPathIsAccessibleByUser() {
        // GIVEN
        given(accessChecker.canExecute(parent, actor)).willReturn(true);
        given(repository.findByNameAndParentAndUser("segment", parent, actor)).willReturn(item);
        given(parentPath.getItem()).willReturn(parent);
        given(parentPath.getItemAsFolder()).willReturn(parent);
        // WHEN
        Path result = pathFactory.resolve(parentPath, "segment", actor);
        // THEN
        assertThat(result).isNotNull()
                .satisfies(p -> assertThat(p.getName()).isEqualTo("segment"))
                .satisfies(p -> assertThat(p.getItem()).isEqualTo(item))
                .satisfies(p -> assertThat(p.getError()).isNull());
    }

    @Test
    void shouldReturnAnInvalidPathWhenCurrentPathIsNotAccessibleByUser() {
        // GIVEN
        given(accessChecker.canExecute(parent, actor)).willReturn(false);
        when(parentPath.getItem()).thenReturn(parent);
        when(parent.toString()).thenReturn("item");
        // WHEN
        Path result = pathFactory.resolve(parentPath, "segment", actor);
        // THEN
        assertThat(result).isNotNull()
                .satisfies(p -> assertThat(p.isError()).isTrue());
    }
}
