package io.github.fherbreteau.functional.mapper;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.model.ItemDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@Service
public class EntityMapper {

    public List<ItemDTO> mapToList(Object value) {
        if (value instanceof Collection<?> coll) {
            return coll.stream()
                    .map(this::map)
                    .filter(Objects::nonNull)
                    .toList();
        }
        return ofNullable(value)
                .map(this::map)
                .map(List::of)
                .orElse(List.of());
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
            if (item instanceof File file) {
                dto.setContentType(file.getContentType());
            }
            return dto;
        }
        return null;
    }

    public ResponseEntity<InputStreamResource> mapStream(Object value, String contentType) {
        MediaType mediaType = ofNullable(contentType)
                .map(MediaType::parseMediaType)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        if (value instanceof InputStream stream) {
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(new InputStreamResource(stream));
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    private String formatAccess(boolean folder, AccessRight ownerAccess, AccessRight groupAccess, AccessRight otherAccess) {
        return (folder ? "d" : "-") + ownerAccess + groupAccess + otherAccess;
    }
}
