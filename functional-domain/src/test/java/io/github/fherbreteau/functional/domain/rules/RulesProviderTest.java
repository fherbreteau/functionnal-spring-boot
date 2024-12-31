package io.github.fherbreteau.functional.domain.rules;

import static org.mockito.Mockito.*;

import io.github.fherbreteau.functional.driven.rules.RuleLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RulesProviderTest {

    private final String expectedRules = """
            group { }
            user { }
            item { }
            """;

    @Mock
    private RuleLoader ruleLoader;

    private RuleProvider ruleProvider;

    @BeforeEach
    void setup() {
        ruleProvider = new RuleProvider(ruleLoader, expectedRules);
    }

    @Test
    void shouldLoadRulesWhenRulesAreNotTheExpectedOnes() {
        // Arrange
        when(ruleLoader.readRules())
                .thenReturn("");

        // Act
        ruleProvider.defineRules();

        // Assert
        verify(ruleLoader).writeRules(expectedRules);
    }

    @Test
    void shouldHandleNoRuleCase() {
        // Arrange
        when(ruleLoader.readRules()).thenReturn(null);

        // Act
        ruleProvider.defineRules();

        // Assert
        verify(ruleLoader).writeRules(expectedRules);
    }

    @Test
    void shouldDoNothingWhenRulesAreTheExpectedOnes() {
        // Arrange
        when(ruleLoader.readRules())
                .thenReturn(expectedRules);

        // Act
        ruleProvider.defineRules();

        // Assert
        verify(ruleLoader, never()).writeRules(expectedRules);
    }
}
