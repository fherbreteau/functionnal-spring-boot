package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UploadCommandTest {
    private UploadCommand command;
    @Mock
    private FileRepository repository;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private InputStream inputStream;

    private User actor;

    @Captor
    private ArgumentCaptor<File> itemCaptor;

    @BeforeEach
    public void setup() {
        File file = File.builder()
                .withName("file")
                .build();
        actor = User.builder("actor").build();
        command = new UploadCommand(repository, contentRepository, file, inputStream, "contentType");
    }

    @Test
    void shouldWriteContentWhenExecutingCommand() {
        // GIVEN
        given(repository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(contentRepository.writeContent(any(), any()))
                .willAnswer(invocation -> Output.success(invocation.getArgument(0)));
        // WHEN
        Output<Item> result = command.execute(actor);
        //THEN
        assertThat(result).isNotNull()
                .extracting(Output::isSuccess, InstanceOfAssertFactories.BOOLEAN)
                .isTrue();
        verify(contentRepository).writeContent(any(), eq(inputStream));
        verify(repository).save(itemCaptor.capture());
        assertThat(itemCaptor.getValue())
                .isInstanceOf(File.class)
                .extracting(File::getContentType)
                .isEqualTo("contentType");
    }
}
