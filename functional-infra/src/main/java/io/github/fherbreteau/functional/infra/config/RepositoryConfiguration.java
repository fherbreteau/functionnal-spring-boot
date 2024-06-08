package io.github.fherbreteau.functional.infra.config;

import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.infra.AccessRightFinder;
import io.github.fherbreteau.functional.infra.impl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class RepositoryConfiguration {

    @Bean
    ItemRepository itemRepository(NamedParameterJdbcTemplate jdbcTemplate, AccessRightFinder accessRightFinder) {
        return new JdbcItemRepository(jdbcTemplate, accessRightFinder);
    }

    @Bean
    AccessRightFinder accessRightFinder(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcAccessRightFinder(jdbcTemplate);
    }

    @Bean
    ContentRepository contentRepository(@Value("${content.repository.path}") String rootPath) {
        return new FSContentRepository(rootPath);
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
