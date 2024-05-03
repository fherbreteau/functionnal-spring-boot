package io.github.fherbreteau.functional.domain.entities;

import java.time.LocalDateTime;
import io.github.fherbreteau.functional.domain.entities.AbstractItem.AbstractBuilder;

public interface Item {

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

    Folder getParent();

    boolean isFolder();

    boolean isFile();

    String getPath();

    <I extends Item, B extends AbstractBuilder<I, B>> AbstractBuilder<I, B> copyBuilder();
}
