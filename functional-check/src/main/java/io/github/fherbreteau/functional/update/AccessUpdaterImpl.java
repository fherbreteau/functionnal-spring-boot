package io.github.fherbreteau.functional.update;

import static io.github.fherbreteau.functional.Entities.*;
import static io.github.fherbreteau.functional.check.Permissions.READ;
import static io.github.fherbreteau.functional.check.Permissions.WRITE;
import static io.github.fherbreteau.functional.check.Permissions.EXECUTE;
import static io.github.fherbreteau.functional.update.Relations.ANYONE_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.authzed.api.v1.*;
import com.authzed.api.v1.PermissionsServiceGrpc.PermissionsServiceBlockingStub;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessUpdaterImpl implements AccessUpdater {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessUpdaterImpl.class);

    private final PermissionsServiceBlockingStub permissionsService;

    public AccessUpdaterImpl(PermissionsServiceBlockingStub permissionsService) {
        this.permissionsService = permissionsService;
    }

    @Override
    public <I extends Item> I createItem(I item) {
        UUID itemHandle = item.getHandle();
        UUID ownerId = item.getOwner().getUserId();
        UUID groupId = item.getGroup().getGroupId();
        List<RelationshipUpdate> updates = new ArrayList<>();
        createAccess(USER, itemHandle, item.getOwnerAccess(), ownerId, updates);
        createAccess(GROUP, itemHandle, item.getGroupAccess(), groupId, updates);
        createAccess(ANYONE, itemHandle, item.getOtherAccess(), null, updates);
        String token = publishRelations(updates);
        LOGGER.info("Item {} created at {}", item, token);
        return item;
    }

    @Override
    public <I extends Item> I updateOwner(I item, User oldOwner) {
        UUID itemHandle = item.getHandle();
        UUID oldOwnerId = oldOwner.getUserId();
        UUID ownerId = item.getOwner().getUserId();
        List<RelationshipUpdate> updates = new ArrayList<>();
        deleteAccess(USER, itemHandle, item.getOwnerAccess(), oldOwnerId, updates);
        createAccess(USER, itemHandle, item.getOwnerAccess(), ownerId, updates);
        String token = publishRelations(updates);
        LOGGER.info("Item {} owner updated at {}", item, token);
        return item;
    }

    @Override
    public <I extends Item> I updateGroup(I item, Group oldGroup) {
        UUID itemHandle = item.getHandle();
        UUID oldGroupId = oldGroup.getGroupId();
        UUID groupId = item.getGroup().getGroupId();
        List<RelationshipUpdate> updates = new ArrayList<>();
        deleteAccess(GROUP, itemHandle, item.getGroupAccess(), oldGroupId, updates);
        createAccess(GROUP, itemHandle, item.getGroupAccess(), groupId, updates);
        String token = publishRelations(updates);
        LOGGER.info("Item {} group updated at {}", item, token);
        return item;
    }

    @Override
    public <I extends Item> I updateOwnerAccess(I item, AccessRight oldOwnerAccess) {
        UUID itemHandle = item.getHandle();
        UUID ownerId = item.getOwner().getUserId();
        List<RelationshipUpdate> updates = new ArrayList<>();
        updateAccess(USER, itemHandle, oldOwnerAccess, item.getOwnerAccess(), ownerId, updates);
        String token = publishRelations(updates);
        LOGGER.info("Item {} access owner updated at {}", item, token);
        return item;
    }

    @Override
    public <I extends Item> I updateGroupAccess(I item, AccessRight oldGroupAccess) {
        UUID itemHandle = item.getHandle();
        UUID groupId = item.getGroup().getGroupId();
        List<RelationshipUpdate> updates = new ArrayList<>();
        updateAccess(GROUP, itemHandle, oldGroupAccess, item.getGroupAccess(), groupId, updates);
        String token = publishRelations(updates);
        LOGGER.info("Item {} access group updated at {}", item, token);
        return item;
    }

    @Override
    public <I extends Item> I updateOtherAccess(I item, AccessRight oldOtherAccess) {
        UUID itemHandle = item.getHandle();
        List<RelationshipUpdate> updates = new ArrayList<>();
        updateAccess(ANYONE, itemHandle, oldOtherAccess, item.getOtherAccess(), null, updates);
        String token = publishRelations(updates);
        LOGGER.info("Item {} access other updated at {}", item, token);
        return item;
    }

    @Override
    public <I extends Item> void deleteItem(I item) {
        UUID itemHandle = item.getHandle();
        UUID ownerId = item.getOwner().getUserId();
        UUID groupId = item.getGroup().getGroupId();
        List<RelationshipUpdate> updates = new ArrayList<>();
        deleteAccess(USER, itemHandle, item.getOwnerAccess(), ownerId, updates);
        deleteAccess(GROUP, itemHandle, item.getGroupAccess(), groupId, updates);
        deleteAccess(ANYONE, itemHandle, item.getOtherAccess(), null, updates);
        String token = publishRelations(updates);
        LOGGER.info("Item {} deleted at {}", item, token);
    }

    private void createAccess(String type, UUID itemId, AccessRight accessRight, UUID subject, List<RelationshipUpdate> updates) {
        String subjectId = Objects.isNull(subject) ? ANYONE_ID : subject.toString();
        if (accessRight.isRead()) {
            updates.add(createRelation(getRelationName(type, READ), type, subjectId, itemId.toString()));
        }
        if (accessRight.isWrite()) {
            updates.add(createRelation(getRelationName(type, WRITE), type, subjectId, itemId.toString()));
        }
        if (accessRight.isExecute()) {
            updates.add(createRelation(getRelationName(type, EXECUTE), type, subjectId, itemId.toString()));
        }
    }

    private void updateAccess(String type, UUID itemId, AccessRight oldAccessRight, AccessRight newAccessRight, UUID subject, List<RelationshipUpdate> updates) {
        String subjectId = Objects.isNull(subject) ? ANYONE_ID : subject.toString();
        updateRelation(getRelationName(type, READ), type, subjectId, itemId.toString(), oldAccessRight.isRead(), newAccessRight.isRead(), updates);
        updateRelation(getRelationName(type, WRITE), type, subjectId, itemId.toString(), oldAccessRight.isWrite(), newAccessRight.isWrite(), updates);
        updateRelation(getRelationName(type, EXECUTE), type, subjectId, itemId.toString(), oldAccessRight.isExecute(), newAccessRight.isExecute(), updates);
    }

    private void deleteAccess(String type, UUID itemId, AccessRight accessRight, UUID subject, List<RelationshipUpdate> updates) {
        String subjectId = Objects.isNull(subject) ? ANYONE : subject.toString();
        if (accessRight.isRead()) {
            updates.add(deleteRelation(getRelationName(type, READ), type, subjectId, itemId.toString()));
        }
        if (accessRight.isWrite()) {
            updates.add(deleteRelation(getRelationName(type, WRITE), type, subjectId, itemId.toString()));
        }
        if (accessRight.isExecute()) {
            updates.add(deleteRelation(getRelationName(type, EXECUTE), type, subjectId, itemId.toString()));
        }
    }

    private String getRelationName(String type, String operation) {
        return String.format("%s_%s", type, operation);
    }

    private void updateRelation(String relation, String type, String subjectId, String itemId, boolean oldValue, boolean newValue, List<RelationshipUpdate> updates) {
        if (oldValue != newValue) {
            if (oldValue) {
                updates.add(deleteRelation(relation, type, subjectId, itemId));
            } else {
                updates.add(createRelation(relation, type, subjectId, itemId));
            }
        }
    }

    private RelationshipUpdate createRelation(String relation, String subjectType, String subjectId, String resourceId) {
        return RelationshipUpdate.newBuilder()
                .setOperation(RelationshipUpdate.Operation.OPERATION_CREATE)
                .setRelationship(Relationship.newBuilder()
                        .setRelation(relation)
                        .setResource(ObjectReference.newBuilder()
                                .setObjectType(ITEM)
                                .setObjectId(resourceId))
                        .setSubject(SubjectReference.newBuilder()
                                .setObject(ObjectReference.newBuilder()
                                        .setObjectType(subjectType)
                                        .setObjectId(subjectId))))
                .build();
    }

    private RelationshipUpdate deleteRelation(String relation, String subjectType, String subjectId, String resourceId) {
        return RelationshipUpdate.newBuilder()
                .setOperation(RelationshipUpdate.Operation.OPERATION_DELETE)
                .setRelationship(Relationship.newBuilder()
                        .setRelation(relation)
                        .setResource(ObjectReference.newBuilder()
                                .setObjectType(ITEM)
                                .setObjectId(resourceId))
                        .setSubject(SubjectReference.newBuilder()
                                .setObject(ObjectReference.newBuilder()
                                        .setObjectType(subjectType)
                                        .setObjectId(subjectId))))
                .build();
    }

    private String publishRelations(List<RelationshipUpdate> relations) {
        WriteRelationshipsRequest request = WriteRelationshipsRequest.newBuilder()
                .addAllUpdates(relations)
                .build();
        try {
            WriteRelationshipsResponse response = permissionsService.writeRelationships(request);
            return response.getWrittenAt().getToken();
        } catch (Exception e) {
            LOGGER.error("Error while publishing relations", e);
            return null;
        }
    }
}
