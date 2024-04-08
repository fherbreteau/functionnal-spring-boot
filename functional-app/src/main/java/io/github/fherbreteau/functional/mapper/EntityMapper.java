package io.github.fherbreteau.functional.mapper;

import io.github.fherbreteau.functional.model.ItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityMapper {

    public List<ItemDTO> mapToList(Object value) {
        return List.of();
    }

    public ItemDTO map(Object value) {
        return null;
    }
}
