package io.github.fherbreteau.functional.mapper;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.model.ItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EntityMapperTest {

    private EntityMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new EntityMapper();
    }

    @Test
    void shouldReturnNullWhenValueIsNotAnItem() {
        ItemDTO dto = mapper.map(new Object());
        assertThat(dto).isNull();
    }

    @Test
    void shouldReturnAnEmptyListWhenValueIsNotAnItemNorACollection() {
        List<ItemDTO> dtos = mapper.mapToList(new Object());
        assertThat(dtos).isEmpty();
    }

    @Test
    void shouldReturnASingleElementListWhenValueIsAnItem() {
        List<ItemDTO> dtos = mapper.mapToList(File.builder()
                .withName("name")
                .withOwner(User.builder("user").build()).build());
        assertThat(dtos).hasSize(1);
    }

    @Test
    void shouldReturnAnUnprocessableEntityWhenInputIsNotAnInputStream() {
        ResponseEntity<InputStreamResource> response = mapper.mapStream(null, null);
        assertThat(response).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
