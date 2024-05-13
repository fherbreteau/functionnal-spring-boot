package io.github.fherbreteau.functional.domain.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    @Test
    void shouldBeAValidPath() {
        Path path = Path.success(Folder.builder().withName("folder").build());
        assertThat(path.getName()).isEqualTo("folder");

        File file = File.builder().build();
        path = Path.success(file);
        assertThat(path).extracting(Path::getAsFile).isEqualTo(file);
    }

    @Test
    void allPathShouldHaveAParentExceptRoot() {
        Folder folder = Folder.builder().withName("folder").withParent(Folder.getRoot()).build();
        File file = File.builder().withName("file").withParent(folder).build();
        Path path = Path.success(file);
        assertThat(path.getParent())
                .isEqualTo(Path.success(folder))
                .extracting(Path::getParent)
                .isEqualTo(Path.ROOT)
                .extracting(Path::getParent)
                .extracting(Path::getError)
                .extracting(Error::getMessage)
                .isEqualTo("Root path has no parent");

        path = Path.error(new Error("error"));
        assertThat(path.getParent())
                .isEqualTo(path)
                .hasSameHashCodeAs(path)
                .isNotEqualTo(Path.ROOT)
                .doesNotHaveSameHashCodeAs(Path.ROOT);

        assertThat((Object) path).isNotEqualTo(file);
    }

    @Test
    void testPathHasRequiredInfoInToString() {
        Path path = Path.success(File.builder().build());
        assertThat(path).hasToString("Path{item='null null:null --------- null'}");
        path = Path.error(new Error("error"));
        assertThat(path).hasToString("Path{error=Error{message='error'}}");
    }
}
