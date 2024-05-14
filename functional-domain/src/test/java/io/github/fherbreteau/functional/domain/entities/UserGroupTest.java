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
        assertThat(group).asString().isEqualTo("group(" + groupId + ")");
        assertThat(Group.group(groupId, "group")).isEqualTo(group)
                .hasSameHashCodeAs(group);

        UUID userId = UUID.randomUUID();
        User user = User.user(userId, "user", group);

        assertThat(user).extracting(User::getName).isEqualTo("user");
        assertThat(user).extracting(User::getUserId).isEqualTo(userId);
        assertThat(user).extracting(User::getGroup).isEqualTo(group);
        assertThat(user).extracting(User::isSuperUser, BOOLEAN).isFalse();
        assertThat(user).asString().isEqualTo("user(" + userId + ")");

        assertThat(Group.root()).extracting(Group::isRoot, BOOLEAN).isTrue();
        assertThat(User.root()).extracting(User::isSuperUser, BOOLEAN).isTrue();

        assertThat(User.user(userId, "user", group)).isEqualTo(user).hasSameHashCodeAs(user);

        user = User.user("user", Group.group("group"));
        assertThat(user).extracting(User::getName).isEqualTo("user");
        assertThat(user).extracting(User::getUserId).isNotEqualTo(userId);
        group = user.getGroup();
        assertThat(group).extracting(Group::getName).isEqualTo("group");
        assertThat(group).extracting(Group::getGroupId).isNotEqualTo(groupId);

        assertThat((Object) user).isNotEqualTo(group);
        assertThat((Object) group).isNotEqualTo(user);
    }

    @Test
    void shouldNotEqualsOrSameHashcode() {
        Group group = Group.group("group");
        User user1 = User.user("user", group);
        User user2 = User.user("user", group);
        assertThat(user1).isNotEqualTo(user2)
                .doesNotHaveSameHashCodeAs(user2);

        UUID userId = UUID.randomUUID();
        user1 = User.user(userId, "user1", group);
        user2 = User.user(userId, "user2", group);
        assertThat(user1).isNotEqualTo(user2)
                .doesNotHaveSameHashCodeAs(user2);

        Group group1 = Group.group("group");
        Group group2 = Group.group("group");
        user1 = User.user(userId, "user", group1);
        user2 = User.user(userId, "user", group2);
        assertThat(user1).isNotEqualTo(user2)
                .doesNotHaveSameHashCodeAs(user2);

        assertThat(group1).isNotEqualTo(group2)
                .doesNotHaveSameHashCodeAs(group2);

        UUID groupId = UUID.randomUUID();
        group1 = Group.group(groupId, "group");
        group2 = Group.group(groupId, "group", group1);
        assertThat(group1).isNotEqualTo(group2)
                .doesNotHaveSameHashCodeAs(group2);

        group2 = Group.group(groupId, "group2");
        assertThat(group1).isNotEqualTo(group2);
    }
}
