package io.github.fherbreteau.functional.rules.init;

import java.util.Objects;

import com.authzed.api.v1.*;
import com.authzed.api.v1.SchemaServiceGrpc.SchemaServiceBlockingStub;
import io.github.fherbreteau.functional.domain.entities.Rules;
import io.github.fherbreteau.functional.driven.rules.RuleLoader;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleLoaderImpl implements RuleLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleLoaderImpl.class);

    private final SchemaServiceBlockingStub schemaService;

    public RuleLoaderImpl(SchemaServiceBlockingStub schemaService) {
        this.schemaService = schemaService;
    }

    public Rules readRules() {
        try {
            ReadSchemaRequest request = ReadSchemaRequest.newBuilder().build();
            ReadSchemaResponse response = schemaService.readSchema(request);
            LOGGER.info("Schema read at {}", response.getReadAt().getToken());
            return new Rules(response.getSchemaText());
        } catch (StatusRuntimeException e) {
            if (Objects.equals(e.getStatus().getCode(), Status.Code.NOT_FOUND)) {
                LOGGER.info("No schema Found on server");
                return null;
            }
            // Rethrow exception
            throw e;
        }
    }

    public void writeRules(Rules rules) {
        WriteSchemaRequest request = WriteSchemaRequest.newBuilder()
                .setSchema(rules.content())
                .build();
        WriteSchemaResponse response = schemaService.writeSchema(request);
        LOGGER.info("Schema written at {}", response.getWrittenAt().getToken());
    }
}
