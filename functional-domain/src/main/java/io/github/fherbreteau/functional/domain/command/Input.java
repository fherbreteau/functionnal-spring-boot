package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;

public class Input {

    private final Item<?, ?> item;
    private final String name;
    private final User user;
    private final Group group;
    private final AccessRight ownerAccess;
    private final AccessRight groupAccess;
    private final AccessRight otherAccess;
    private final byte[] content;

    public Input(Item<?, ?> item) {
        this(item, (String) null);
    }

    public Input(Item<?, ?> item, String name) {
        this(item, name, null, null, null, null, null, null);
    }

    public Input(Item<?, ?> item, User user) {
        this(item, null, user, null, null, null, null, null);
    }

    public Input(Item<?, ?> item, Group group) {
        this(item, null, null, group, null, null, null, null);
    }

    public Input(Item<?, ?> item, byte[] content) {
        this(item, null, null, null, null, null, null, content);
    }

    public Input(Item<?, ?> item, AccessRight ownerAccess, AccessRight groupAccess, AccessRight otherAccess) {
        this(item, null, null, null, ownerAccess, groupAccess, otherAccess, null);
    }

    private Input(Item<?, ?> item, String name, User user, Group group, AccessRight ownerAccess, AccessRight groupAccess, AccessRight otherAccess, byte[] content) {
        this.item = item;
        this.name = name;
        this.user = user;
        this.group = group;
        this.ownerAccess = ownerAccess;
        this.groupAccess = groupAccess;
        this.otherAccess = otherAccess;
        this.content = content;
    }

    public Item<?, ?> getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public Group getGroup() {
        return group;
    }

    public AccessRight[] getAccesses() {
        return new AccessRight[]{ownerAccess, groupAccess, otherAccess};
    }

    public AccessRight getOwnerAccess() {
        return ownerAccess;
    }

    public AccessRight getGroupAccess() {
        return groupAccess;
    }

    public AccessRight getOtherAccess() {
        return otherAccess;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Input{" +
                "item=" + item +
                ", name='" + name + '\'' +
                ", user=" + user +
                ", group=" + group +
                ", ownerAccess=" + ownerAccess +
                ", groupAccess=" + groupAccess +
                ", otherAccess=" + otherAccess +
                ", content=<redacted>" +
                '}';
    }
}
