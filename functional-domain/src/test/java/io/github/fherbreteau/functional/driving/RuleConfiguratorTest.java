package io.github.fherbreteau.functional.driving;

import static org.mockito.Mockito.verify;

import io.github.fherbreteau.functional.domain.rules.RuleProvider;
import io.github.fherbreteau.functional.driving.impl.RuleConfiguratorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RuleConfiguratorTest {
    @Mock
    private RuleProvider ruleProvider;

    @InjectMocks
    private RuleConfiguratorImpl configurator;

    @Test
    void shouldDelegateToTheProvider() {
        // Arrange
        // Act
        configurator.defineRules();
        // Assert
        verify(ruleProvider).defineRules();
    }
}
