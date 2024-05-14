package io.github.fherbreteau.functional.service;

import io.github.fherbreteau.functional.driving.AccessParserService;
import io.github.fherbreteau.functional.driving.FileService;
import io.github.fherbreteau.functional.exception.CommandException;
import io.github.fherbreteau.functional.mapper.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class FileSystemServiceTest {

    private FileSystemService service;
    @Mock
    private FileService fileService;
    @Mock
    private EntityMapper entityMapper;
    @Mock
    private AccessParserService accessParserService;

    @BeforeEach
    public void setup() {
        service = new FileSystemService(fileService, entityMapper, accessParserService);
    }

    @Test
    void shouldThrowACommandExceptionWhenAnExceptionIfEmitted() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        given(file.getInputStream()).willThrow(IOException.class);
        assertThatThrownBy(() -> service.upload("/path", file, "username"))
                .isInstanceOf(CommandException.class);
    }
}
