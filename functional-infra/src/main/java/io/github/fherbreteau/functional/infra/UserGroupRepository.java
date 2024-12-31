package io.github.fherbreteau.functional.infra;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;

public interface UserGroupRepository {

    void create(User user, List<UUID> groupIds);

    void create(List<UUID> userIds, Group group);

    List<UUID> deleteByUser(User user);

    List<UUID> deleteByGroup(Group group);
}
