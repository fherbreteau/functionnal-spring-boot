package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;

@SuppressWarnings("rawtypes")
public final class Input {

    private final Item item;
    private final String name;
    private final User user;
    private final Group group;
    private final AccessRight ownerAccess;
    private final AccessRight groupAccess;
    private final AccessRight otherAccess;
    private final byte[] content;

    private Input(Builder builder) {
        this.item = builder.item;
        this.name = builder.name;
        this.user = builder.user;
        this.group = builder.group;
        this.ownerAccess = builder.ownerAccess;
        this.groupAccess = builder.groupAccess;
        this.otherAccess = builder.otherAccess;
        this.content = builder.content;
    }

    public Item getItem() {
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

    public static Builder builder(Item item) {
        return new Builder(item);
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

    public static final class Builder {

        private final Item item;
        private String name;
        private User user;
        private Group group;
        private AccessRight ownerAccess;
        private AccessRight groupAccess;
        private AccessRight otherAccess;
        private byte[] content;

        private Builder(Item<?, ?> item) {
            this.item = item;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withGroup(Group group) {
            this.group = group;
            return this;
        }

        public Builder withOwnerAccess(AccessRight ownerAccess) {
            this.ownerAccess = ownerAccess;
            return this;
        }

        public Builder withGroupAccess(AccessRight groupAccess) {
            this.groupAccess = groupAccess;
            return this;
        }

        public Builder withOtherAccess(AccessRight otherAccess) {
            this.otherAccess = otherAccess;
            return this;
        }

        public Builder withContent(byte[] content) {
            this.content = content;
            return this;
        }

        public Input build() {
            return new Input(this);
        }
    }
}
