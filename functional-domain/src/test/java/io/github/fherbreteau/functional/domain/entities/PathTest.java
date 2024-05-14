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
        assertThat(Path.success(file)).isEqualTo(path);

        Error error = new Error("error");
        path = Path.error(error);
        assertThat(Path.error(error)).isEqualTo(path);
        assertThat(path.getError()).isEqualTo(error).hasSameHashCodeAs(error);

        assertThat(Path.error(new Error("err"))).isNotEqualTo(path).doesNotHaveSameHashCodeAs(path);
        assertThat(path.getError()).isNotEqualTo(new Object());
        assertThat(new Error("error")).isEqualTo(error);
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

    @Test
    void testPathCanHaveAContentType() {
        Path path = Path.success(File.builder().withContentType("Content-Type").build());
        assertThat(path.getContentType()).isEqualTo("Content-Type");
        path = Path.success(Folder.builder().build());
        assertThat(path.getContentType()).isNull();
    }
}
