package io.github.fherbreteau.functional.domain.entities;

import java.time.LocalDateTime;

public interface Item<T extends Item<T,B>, B extends AbstractItem.Builder<T, B>> {

    String ROOT = "";

    String getName();

    User getOwner();

    Group getGroup();

    AccessRight getOwnerAccess();

    AccessRight getGroupAccess();

    AccessRight getOtherAccess();

    LocalDateTime getCreated();

    LocalDateTime getLastModified();

    LocalDateTime getLastAccessed();

    Item<? , ?> getParent();

    boolean isFolder();

    boolean isFile();

    String getPath();

    B copyBuilder();
}
