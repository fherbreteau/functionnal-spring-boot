package io.github.fherbreteau.functional.config;

import io.github.fherbreteau.functional.domain.path.factory.impl.ComplexSegmentPathParserPathFactory;
import io.github.fherbreteau.functional.domain.path.factory.impl.CurrentSegmentPathParserFactory;
import io.github.fherbreteau.functional.domain.path.factory.impl.EmptySegmentPathParserFactory;
import io.github.fherbreteau.functional.domain.path.factory.impl.InvalidPathParserFactory;
import io.github.fherbreteau.functional.domain.path.factory.impl.ParentSegmentPathParserFactory;
import io.github.fherbreteau.functional.domain.path.factory.impl.SingleSegmentPathParserFactory;
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
