package io.github.fherbreteau.functional.config;

import io.github.fherbreteau.functional.domain.path.factory.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathFactoryConfiguration {

    @Bean
    public ComplexSegmentPathFactory complexSegmentPathFactory() {
        return new ComplexSegmentPathFactory();
    }

    @Bean
    public CurrentSegmentPathFactory currentSegmentPathFactory() {
        return new CurrentSegmentPathFactory();
    }

    @Bean
    public EmptySegmentPathFactory emptySegmentPathFactory() {
        return new EmptySegmentPathFactory();
    }

    @Bean
    public InvalidPathFactory invalidPathFactory() {
        return new InvalidPathFactory();
    }

    @Bean
    public ParentSegmentPathFactory parentSegmentPathFactory() {
        return new ParentSegmentPathFactory();
    }

    @Bean
    public SingleSegmentPathFactory singleSegmentPathFactory() {
        return new SingleSegmentPathFactory();
    }
}
