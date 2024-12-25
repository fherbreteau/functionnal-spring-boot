package io.github.fherbreteau.functional.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;

import org.junit.jupiter.api.Test;

class AccessRightTest {

    @Test
    void shouldBeCorrectlyConfigured() {
        AccessRight right = AccessRight.none();
        assertThat(right).extracting(AccessRight::isRead, BOOLEAN).isFalse();
        assertThat(right).extracting(AccessRight::isWrite, BOOLEAN).isFalse();
        assertThat(right).extracting(AccessRight::isExecute, BOOLEAN).isFalse();

        right = AccessRight.readOnly();
        assertThat(right).extracting(AccessRight::isRead, BOOLEAN).isTrue();
        assertThat(right).extracting(AccessRight::isWrite, BOOLEAN).isFalse();
        assertThat(right).extracting(AccessRight::isExecute, BOOLEAN).isFalse();

        right = AccessRight.writeOnly();
        assertThat(right).extracting(AccessRight::isRead, BOOLEAN).isFalse();
        assertThat(right).extracting(AccessRight::isWrite, BOOLEAN).isTrue();
        assertThat(right).extracting(AccessRight::isExecute, BOOLEAN).isFalse();

        right = AccessRight.executeOnly();
        assertThat(right).extracting(AccessRight::isRead, BOOLEAN).isFalse();
        assertThat(right).extracting(AccessRight::isWrite, BOOLEAN).isFalse();
        assertThat(right).extracting(AccessRight::isExecute, BOOLEAN).isTrue();

        right = AccessRight.readWrite();
        assertThat(right).extracting(AccessRight::isRead, BOOLEAN).isTrue();
        assertThat(right).extracting(AccessRight::isWrite, BOOLEAN).isTrue();
        assertThat(right).extracting(AccessRight::isExecute, BOOLEAN).isFalse();

        right = AccessRight.readExecute();
        assertThat(right).extracting(AccessRight::isRead, BOOLEAN).isTrue();
        assertThat(right).extracting(AccessRight::isWrite, BOOLEAN).isFalse();
        assertThat(right).extracting(AccessRight::isExecute, BOOLEAN).isTrue();

        right = AccessRight.writeExecute();
        assertThat(right).extracting(AccessRight::isRead, BOOLEAN).isFalse();
        assertThat(right).extracting(AccessRight::isWrite, BOOLEAN).isTrue();
        assertThat(right).extracting(AccessRight::isExecute, BOOLEAN).isTrue();

        right = AccessRight.full();
        assertThat(right).extracting(AccessRight::isRead, BOOLEAN).isTrue();
        assertThat(right).extracting(AccessRight::isWrite, BOOLEAN).isTrue();
        assertThat(right).extracting(AccessRight::isExecute, BOOLEAN).isTrue();
    }

    @Test
    void testThatRemovedElementChangeAccesses() {
        AccessRight right = AccessRight.full();

        AccessRight changed = right.removeExecute();
        assertThat(changed).extracting(AccessRight::isRead, BOOLEAN).isTrue();
        assertThat(changed).extracting(AccessRight::isWrite, BOOLEAN).isTrue();
        assertThat(changed).extracting(AccessRight::isExecute, BOOLEAN).isFalse();

        changed = right.removeWrite();
        assertThat(changed).extracting(AccessRight::isRead, BOOLEAN).isTrue();
        assertThat(changed).extracting(AccessRight::isWrite, BOOLEAN).isFalse();
        assertThat(changed).extracting(AccessRight::isExecute, BOOLEAN).isTrue();

        changed = right.removeRead();
        assertThat(changed).extracting(AccessRight::isRead, BOOLEAN).isFalse();
        assertThat(changed).extracting(AccessRight::isWrite, BOOLEAN).isTrue();
        assertThat(changed).extracting(AccessRight::isExecute, BOOLEAN).isTrue();
    }

    @Test
    void testThatHashcodeAndEqualsAreCorrectlyChecked() {
        AccessRight right = AccessRight.none();

        assertThat((Object) right).isNotEqualTo("");
        AccessRight left = right.addRead();
        assertThat(right).isNotEqualTo(left).doesNotHaveSameHashCodeAs(left);
        left = right.addWrite();
        assertThat(right).isNotEqualTo(left).doesNotHaveSameHashCodeAs(left);
        left = right.addExecute();
        assertThat(right).isNotEqualTo(left).doesNotHaveSameHashCodeAs(left);
    }
}
