package io.github.fherbreteau.functional.rules.init;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authzed.api.v1.*;
import com.authzed.api.v1.SchemaServiceGrpc.SchemaServiceBlockingStub;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RuleLoaderTest {
    @Mock
    private SchemaServiceBlockingStub schemaService;

    @InjectMocks
    private RuleLoaderImpl ruleLoader;

    @Captor
    private ArgumentCaptor<ReadSchemaRequest> readCaptor;

    @Captor
    private ArgumentCaptor<WriteSchemaRequest> writeCaptor;

    @Test
    void shouldExtractFromSpiceDbTheRules() {
        // Arrange
        when(schemaService.readSchema(any()))
                .thenReturn(ReadSchemaResponse.newBuilder()
                        .setReadAt(ZedToken.newBuilder().setToken("token"))
                        .setSchemaText("schema")
                        .build());

        // Act
        String result = ruleLoader.readRules();

        // Assert
        assertThat(result).isEqualTo("schema");
        verify(schemaService).readSchema(readCaptor.capture());
        assertThat(readCaptor.getValue())
                .isEqualTo(ReadSchemaRequest.newBuilder().build());
    }

    @Test
    void shouldHandleNotFoundStatusException() {
        // Arrange
        when(schemaService.readSchema(any()))
                .thenThrow(new StatusRuntimeException(Status.NOT_FOUND));

        // Act
        String result = ruleLoader.readRules();

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void shouldNotHandleOtherStatusException() {
        // Arrange
        when(schemaService.readSchema(any()))
                .thenThrow(new StatusRuntimeException(Status.UNAVAILABLE));

        // Act
        assertThatThrownBy(() -> ruleLoader.readRules())
        // Assert
                .isInstanceOf(StatusRuntimeException.class);
    }

    @Test
    void shouldInsertInSpiceDbTheRules() {
        // Arrange
        when(schemaService.writeSchema(any()))
                .thenReturn(WriteSchemaResponse.newBuilder()
                        .setWrittenAt(ZedToken.newBuilder().setToken("token"))
                        .build());

        // Act
        ruleLoader.writeRules("schema");

        // Arrange
        verify(schemaService).writeSchema(writeCaptor.capture());
        assertThat(writeCaptor.getValue())
                .extracting(WriteSchemaRequest::getSchema)
                .isEqualTo("schema");
    }
}
