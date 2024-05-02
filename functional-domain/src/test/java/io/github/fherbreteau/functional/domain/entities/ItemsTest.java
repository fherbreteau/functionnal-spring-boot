package io.github.fherbreteau.functional.domain.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.*;

class ItemsTest {

    @Test
    void testItemsHaveTheGoodTypes() {
        File file = File.builder().withContent(new byte[0]).build();
        assertThat(file).extracting(Item::isFile, BOOLEAN).isTrue();
        assertThat(file).extracting(Item::isFolder, BOOLEAN).isFalse();
        assertThat(file).extracting("content", BYTE_ARRAY).isNotNull().isEmpty();
        assertThat(file.copyBuilder().build())
                .isEqualTo(file)
                .hasSameHashCodeAs(file);

        assertThat(file).extracting(Item::getCreated, LOCAL_DATE_TIME).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(file).extracting(Item::getLastAccessed, LOCAL_DATE_TIME).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(file).extracting(Item::getLastModified, LOCAL_DATE_TIME).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());

        Folder folder = Folder.builder().build();
        assertThat(folder).extracting(Item::isFile, BOOLEAN).isFalse();
        assertThat(folder).extracting(Item::isFolder, BOOLEAN).isTrue();
        assertThat(folder.copyBuilder().build())
                .isEqualTo(folder)
                .hasSameHashCodeAs(folder);

        assertThat((Item) file).isNotEqualTo(folder);
        assertThat((Item) folder).isNotEqualTo(file);
        assertThat((Object) file).isNotEqualTo(new Object());
        assertThat((Object) folder).isNotEqualTo(new Object());
    }

    @Test
    void testThatAllItemHaveAPath() {
        Folder folder = Folder.builder().withName("folder").withParent(Folder.getRoot()).build();
        File file = File.builder().withName("file").withParent(folder).build();
        assertThat(file).extracting(AbstractItem::getPath, STRING).isEqualTo("/folder/file");
    }
}
