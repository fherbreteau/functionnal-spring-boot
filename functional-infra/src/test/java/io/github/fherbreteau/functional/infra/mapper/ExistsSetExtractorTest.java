package io.github.fherbreteau.functional.infra.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ExistsSetExtractorTest {
    @Mock
    private ResultSet resultSet;

    private final ExistsSetExtractor existsSetExtractor = new ExistsSetExtractor();

    @Test
    void shouldReturnTrueWhenResultSetHasResults() throws SQLException {
        given(resultSet.next()).willReturn(true);
        assertThat(existsSetExtractor.extractData(resultSet)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenResultSetHasNoResult() throws SQLException {
        given(resultSet.next()).willReturn(false);
        assertThat(existsSetExtractor.extractData(resultSet)).isFalse();
    }
}
