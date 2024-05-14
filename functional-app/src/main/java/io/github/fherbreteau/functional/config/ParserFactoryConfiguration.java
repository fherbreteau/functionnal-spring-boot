package io.github.fherbreteau.functional.config;

import io.github.fherbreteau.functional.domain.access.factory.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserFactoryConfiguration {

    @Bean
    public AddAccessParserFactory addAccessParserFactory() {
        return new AddAccessParserFactory();
    }

    @Bean
    public AttributionAccessParserFactory attributionAccessParserFactory() {
        return new AttributionAccessParserFactory();
    }

    @Bean
    public UnsupportedAccessParserFactory errorAccessParserFactory() {
        return new UnsupportedAccessParserFactory();
    }

    @Bean
    public EveryoneAccessParserFactory everyoneAccessParserFactory() {
        return new EveryoneAccessParserFactory();
    }

    @Bean
    public ExecuteAccessParserFactory executeAccessParserFactory() {
        return new ExecuteAccessParserFactory();
    }

    @Bean
    public FullAccessParserFactory fullAccessParserFactory() {
        return new FullAccessParserFactory();
    }

    @Bean
    public GroupAccessParserFactory groupAccessParserFactory() {
        return new GroupAccessParserFactory();
    }

    @Bean
    public OtherAccessParserFactory otherAccessParserFactory() {
        return new OtherAccessParserFactory();
    }

    @Bean
    public OwnerAccessParserFactory ownerAccessParserFactory() {
        return new OwnerAccessParserFactory();
    }

    @Bean
    public ReadAccessParserFactory readAccessParserFactory() {
        return new ReadAccessParserFactory();
    }

    @Bean
    public RemoveAccessParserFactory removeAccessParserFactory() {
        return new RemoveAccessParserFactory();
    }

    @Bean
    public RightAccessParserFactory rightAccessParserFactory() {
        return new RightAccessParserFactory();
    }

    @Bean
    public SetAccessParserFactory setAccessParserFactory() {
        return new SetAccessParserFactory();
    }

    @Bean
    public WriteAccessParserFactory writeAccessParserFactory() {
        return new WriteAccessParserFactory();
    }
}
