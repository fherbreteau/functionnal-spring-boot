package io.github.fherbreteau.functional.config;

import io.github.fherbreteau.functional.domain.path.factory.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathFactoryConfiguration {

    @Bean
    public ComplexSegmentPathParserPathFactory complexSegmentPathFactory() {
        return new ComplexSegmentPathParserPathFactory();
    }

    @Bean
    public CurrentSegmentPathParserFactory currentSegmentPathFactory() {
        return new CurrentSegmentPathParserFactory();
    }

    @Bean
    public EmptySegmentPathParserFactory emptySegmentPathFactory() {
        return new EmptySegmentPathParserFactory();
    }

    @Bean
    public InvalidPathParserFactory invalidPathFactory() {
        return new InvalidPathParserFactory();
    }

    @Bean
    public ParentSegmentPathParserFactory parentSegmentPathFactory() {
        return new ParentSegmentPathParserFactory();
    }

    @Bean
    public SingleSegmentPathParserFactory singleSegmentPathFactory() {
        return new SingleSegmentPathParserFactory();
    }
}
