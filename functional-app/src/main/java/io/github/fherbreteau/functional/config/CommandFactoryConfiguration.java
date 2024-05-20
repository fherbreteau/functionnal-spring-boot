package io.github.fherbreteau.functional.config;

import io.github.fherbreteau.functional.domain.command.factory.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandFactoryConfiguration {

    @Bean
    public ChangeGroupCommandFactory changeGroupCommandFactory() {
        return new ChangeGroupCommandFactory();
    }

    @Bean
    public ChangeModeCommandFactory changeModeCommandFactory() {
        return new ChangeModeCommandFactory();
    }

    @Bean
    public ChangeOwnerCommandFactory changeOwnerCommandFactory() {
        return new ChangeOwnerCommandFactory();
    }

    @Bean
    public CreateItemCommandFactory createItemCommandFactory() {
        return new CreateItemCommandFactory();
    }

    @Bean
    public DownloadCommandFactory downloadCommandFactory() {
        return new DownloadCommandFactory();
    }

    @Bean
    public ListChildrenCommandFactory listChildrenCommandFactory() {
        return new ListChildrenCommandFactory();
    }

    @Bean
    public UnsupportedItemCommandFactory unsupportedCommandFactory() {
        return new UnsupportedItemCommandFactory();
    }

    @Bean
    public UploadCommandFactory uploadCommandFactory() {
        return new UploadCommandFactory();
    }
}
