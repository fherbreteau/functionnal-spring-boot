package io.github.fherbreteau.functional.config;

import java.nio.file.FileSystems;

import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.infra.impl.FSContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfiguration {

    @Bean
    ContentRepository contentRepository(@Value("${content.repository.path}") String rootPath) {
        return new FSContentRepository(rootPath, FileSystems.getDefault());
    }
}
