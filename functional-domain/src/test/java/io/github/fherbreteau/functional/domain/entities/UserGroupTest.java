package io.github.fherbreteau.functional.domain.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;

class UserGroupTest {

    @Test
    void shouldBeCorrectlyConfigured() {
        UUID groupId = UUID.randomUUID();
        Group group = Group.group(groupId, "group");

        assertThat(group).extracting(Group::getName).isEqualTo("group");
        assertThat(group).extracting(Group::getGroupId).isEqualTo(groupId);
        assertThat(group).extracting(Group::getParent).isEqualTo(Group.root());
        assertThat(group).extracting(Group::isRoot, BOOLEAN).isFalse();
        assertThat(group).asString().isEqualTo(group.getName());
        assertThat(Group.group(groupId, "group")).hasSameHashCodeAs(group);

        UUID userId = UUID.randomUUID();
        User user = User.user(userId, "user", group);

        assertThat(user).extracting(User::getName).isEqualTo("user");
        assertThat(user).extracting(User::getUserId).isEqualTo(userId);
        assertThat(user).extracting(User::getGroup).isEqualTo(group);
        assertThat(user).extracting(User::isSuperUser, BOOLEAN).isFalse();
        assertThat(user).asString().isEqualTo(user.getName());

        assertThat(Group.root()).extracting(Group::isRoot, BOOLEAN).isTrue();
        assertThat(User.root()).extracting(User::isSuperUser, BOOLEAN).isTrue();

        assertThat(User.user(userId, "user", group)).hasSameHashCodeAs(user);

        user = User.user("user", Group.group("group"));
        assertThat(user).extracting(User::getName).isEqualTo("user");
        assertThat(user).extracting(User::getUserId).isNotEqualTo(userId);
        group = user.getGroup();
        assertThat(group).extracting(Group::getName).isEqualTo("group");
        assertThat(group).extracting(Group::getGroupId).isNotEqualTo(groupId);

        assertThat((Object) user).isNotEqualTo(group);
        assertThat((Object) group).isNotEqualTo(user);
    }
}
