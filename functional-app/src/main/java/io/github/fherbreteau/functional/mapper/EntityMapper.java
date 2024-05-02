package io.github.fherbreteau.functional.mapper;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.model.ItemDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class EntityMapper {

    public List<ItemDTO> mapToList(Object value) {
        if (value instanceof Collection<?> coll) {
            return coll.stream().map(this::map).toList();
        }
        return List.of(map(value));
    }

    public ItemDTO map(Object value) {
        if (value instanceof Item item) {
            ItemDTO dto = new ItemDTO();
            dto.setName(item.getName());
            dto.setOwner(item.getOwner().getName());
            dto.setGroup(item.getGroup().getName());
            dto.setAccess(formatAccess(item.isFolder(), item.getOwnerAccess(), item.getGroupAccess(), item.getOtherAccess()));
            dto.setCreated(item.getCreated());
            dto.setModified(item.getLastModified());
            dto.setAccessed(item.getLastAccessed());
            return dto;
        }
        return null;
    }

    private String formatAccess(boolean folder, AccessRight ownerAccess, AccessRight groupAccess, AccessRight otherAccess) {
        return (folder ? "d" : "-") + ownerAccess + groupAccess + otherAccess;
    }
}
