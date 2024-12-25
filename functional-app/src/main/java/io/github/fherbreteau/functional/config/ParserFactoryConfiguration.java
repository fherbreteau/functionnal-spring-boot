package io.github.fherbreteau.functional.config;

import io.github.fherbreteau.functional.domain.access.factory.impl.AddAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.AttributionAccessParserAccessFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.EveryoneAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.ExecuteAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.FullAccessParserAccessFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.GroupAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.OtherAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.OwnerAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.ReadAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.RemoveAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.RightAccessParserAccessFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.SetAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.UnsupportedAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.impl.WriteAccessParserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserFactoryConfiguration {

    @Bean
    public AddAccessParserFactory addAccessParserFactory() {
        return new AddAccessParserFactory();
    }

    @Bean
    public AttributionAccessParserAccessFactory attributionAccessParserFactory() {
        return new AttributionAccessParserAccessFactory();
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
    public FullAccessParserAccessFactory fullAccessParserFactory() {
        return new FullAccessParserAccessFactory();
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
    public RightAccessParserAccessFactory rightAccessParserFactory() {
        return new RightAccessParserAccessFactory();
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
