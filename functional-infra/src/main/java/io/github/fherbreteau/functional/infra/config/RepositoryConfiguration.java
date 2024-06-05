package io.github.fherbreteau.functional.infra.config;

import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.ItemRepository;
import io.github.fherbreteau.functional.driven.UserRepository;
import io.github.fherbreteau.functional.infra.ItemFinder;
import io.github.fherbreteau.functional.infra.impl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class RepositoryConfiguration {

    @Bean
    ItemRepository itemRepository(NamedParameterJdbcTemplate jdbcTemplate, ItemFinder itemFinder) {
        return new JdbcItemRepository(jdbcTemplate, itemFinder);
    }

    @Bean
    ItemFinder itemFinder(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcItemFinder(jdbcTemplate);
    }

    @Bean
    ContentRepository contentRepository(@Value("${content.repository.path}") String rootPath, ItemFinder itemFinder) {
        return new FSContentRepository(rootPath, itemFinder);
    }

    @Bean
    UserRepository userRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcUserRepository(jdbcTemplate);
    }

    @Bean
    GroupRepository groupRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcGroupRepository(jdbcTemplate);
    }
}
