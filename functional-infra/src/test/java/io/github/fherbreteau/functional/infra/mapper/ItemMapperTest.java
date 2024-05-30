package io.github.fherbreteau.functional.infra.mapper;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {
    @Mock
    private ResultSet resultSet;

    private final ItemMapper itemMapper = new ItemMapper();

    @Test
    void shouldMapAnFileWithResultSetContent() throws SQLException {
        given(resultSet.getString(COL_TYPE)).willReturn("File");
        given(resultSet.getString(COL_NAME)).willReturn("name");
        given(resultSet.getString(UserSQLConstant.COL_NAME)).willReturn("name");
        given(resultSet.getObject(UserSQLConstant.COL_ID, UUID.class)).willReturn(UUID.randomUUID());
        given(resultSet.getString(GroupSQLConstant.COL_NAME)).willReturn("name");
        given(resultSet.getObject(GroupSQLConstant.COL_ID, UUID.class)).willReturn(UUID.randomUUID());
        given(resultSet.getObject(COL_CREATED_AT, LocalDateTime.class)).willReturn(LocalDateTime.now());
        given(resultSet.getObject(COL_MODIFIED_AT, LocalDateTime.class)).willReturn(LocalDateTime.now());
        given(resultSet.getObject(COL_ACCESSED_AT, LocalDateTime.class)).willReturn(LocalDateTime.now());

        Item result = itemMapper.mapRow(resultSet, 0);

        assertThat(result).isNotNull()
                .extracting(Item::getName).isEqualTo("name");
        assertThat(result).extracting(Item::getOwner)
                .extracting(User::getName).isEqualTo("name");
        assertThat(result).extracting(Item::getOwner)
                .extracting(User::getUserId).isNotNull();
        assertThat(result).extracting(Item::getGroup)
                .extracting(Group::getName).isEqualTo("name");
        assertThat(result).extracting(Item::getGroup)
                .extracting(Group::getGroupId).isNotNull();
        assertThat(result).extracting(Item::getCreated, LOCAL_DATE_TIME)
                .isEqualToIgnoringNanos(LocalDateTime.now());
        assertThat(result).extracting(Item::getLastModified, LOCAL_DATE_TIME)
                .isEqualToIgnoringNanos(LocalDateTime.now());
        assertThat(result).extracting(Item::getLastAccessed, LOCAL_DATE_TIME)
                .isEqualToIgnoringNanos(LocalDateTime.now());
    }
}
