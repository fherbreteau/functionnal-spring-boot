package io.github.fherbreteau.functional.rules.update;

import static io.github.fherbreteau.functional.rules.Entities.*;
import static io.github.fherbreteau.functional.rules.update.Relations.MEMBER;
import static io.github.fherbreteau.functional.rules.update.Relations.ADMIN;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.authzed.api.v1.*;
import com.authzed.api.v1.PermissionsServiceGrpc.PermissionsServiceBlockingStub;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserUpdaterImpl implements UserUpdater {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserUpdaterImpl.class);

    private final PermissionsServiceBlockingStub permissionsService;

    public UserUpdaterImpl(PermissionsServiceBlockingStub permissionsService) {
        this.permissionsService = permissionsService;
    }

    @Override
    public User createUser(User user) {
        List<RelationshipUpdate> updates = new ArrayList<>();
        if (user.isSuperUser()) {
            updates.add(createRelation(ADMIN, user.getUserId(), USER, user.getUserId()));
        }
        user.getGroups().stream()
                .map(Group::getGroupId)
                .map(group -> createRelation(MEMBER, user.getUserId(), GROUP, group))
                .forEach(updates::add);
        String token = publishRelations(updates);
        LOGGER.info("User {} created at {}", user, token);
        return user;
    }

    @Override
    public User updateUser(User oldUser, User newUser) {
        boolean userIdNotChanged = Objects.equals(oldUser.getUserId(), newUser.getUserId());
        List<RelationshipUpdate> updates = new ArrayList<>();
        List<Group> toRemove = new ArrayList<>(oldUser.getGroups());
        if (userIdNotChanged) {
            toRemove.removeAll(newUser.getGroups());
        }
        toRemove.stream()
                .map(Group::getGroupId)
                .map(group -> deleteRelation(MEMBER, oldUser.getUserId(), GROUP, group))
                .forEach(updates::add);
        List<Group> toAdd = new ArrayList<>(newUser.getGroups());
        if (userIdNotChanged) {
            toAdd.removeAll(oldUser.getGroups());
        }
        toAdd.stream()
                .map(Group::getGroupId)
                .map(group -> createRelation(MEMBER, newUser.getUserId(), GROUP, group))
                .forEach(updates::add);
        String token = publishRelations(updates);
        LOGGER.info("User {} updated at {}", newUser, token);
        return newUser;
    }

    @Override
    public void deleteUser(User user) {
        List<RelationshipUpdate> updates = new ArrayList<>();
        if (user.isSuperUser()) {
            updates.add(deleteRelation(ADMIN, user.getUserId(), USER, user.getUserId()));
        }
        user.getGroups().stream()
                .map(Group::getGroupId)
                .map(group -> deleteRelation(MEMBER, user.getUserId(), GROUP, group))
                .forEach(updates::add);
        String token = publishRelations(updates);
        LOGGER.info("User {} deleted at {}", user, token);
    }

    @Override
    public Group createGroup(Group group) {
        // Nothing to do here
        return group;
    }

    @Override
    public Group updateGroup(Group oldGroup, Group newGroup) {
        List<RelationshipUpdate> updates = new ArrayList<>();
        if (!Objects.equals(oldGroup.getGroupId(), newGroup.getGroupId())) {
            StreamSupport.stream(userLookup(oldGroup.getGroupId()).spliterator(), false)
                    .map(LookupSubjectsResponse::getSubject)
                    .map(ResolvedSubject::getSubjectObjectId)
                    .map(UUID::fromString)
                    .flatMap(userId -> Stream.of(
                            deleteRelation(MEMBER, userId, GROUP, oldGroup.getGroupId()),
                            createRelation(MEMBER, userId, GROUP, newGroup.getGroupId())
                    )).forEach(updates::add);
        }
        String token = publishRelations(updates);
        LOGGER.info("Group {} updated at {}", newGroup, token);
        return newGroup;
    }

    @Override
    public void deleteGroup(Group group) {
        DeleteRelationshipsRequest request = DeleteRelationshipsRequest.newBuilder()
                .setRelationshipFilter(RelationshipFilter.newBuilder()
                        .setOptionalRelation(MEMBER)
                        .setResourceType(GROUP)
                        .setOptionalResourceId(group.getGroupId().toString()))
                .build();
        try {
            DeleteRelationshipsResponse response = permissionsService.deleteRelationships(request);
            if (response.getDeletionProgress() != DeleteRelationshipsResponse.DeletionProgress.DELETION_PROGRESS_COMPLETE) {
                LOGGER.warn("Deletion of relation not completed : {}", response.getDeletionProgress());
            }
            LOGGER.info("Group {} deleted at {}", group, response.getDeletedAt().getToken());
        } catch (Exception e) {
            LOGGER.error("Error while deleting relations", e);
        }
    }

    private RelationshipUpdate createRelation(String relation, UUID subjectId, String resourceType, UUID resourceId) {
        return RelationshipUpdate.newBuilder()
                .setOperation(RelationshipUpdate.Operation.OPERATION_CREATE)
                .setRelationship(Relationship.newBuilder()
                        .setRelation(relation)
                        .setResource(ObjectReference.newBuilder()
                                .setObjectType(resourceType)
                                .setObjectId(resourceId.toString()))
                        .setSubject(SubjectReference.newBuilder()
                                .setObject(ObjectReference.newBuilder()
                                        .setObjectType(USER)
                                        .setObjectId(subjectId.toString()))))
                .build();
    }

    private RelationshipUpdate deleteRelation(String relation, UUID subjectId, String resourceType, UUID resourceId) {
        return RelationshipUpdate.newBuilder()
                .setOperation(RelationshipUpdate.Operation.OPERATION_DELETE)
                .setRelationship(Relationship.newBuilder()
                        .setRelation(relation)
                        .setResource(ObjectReference.newBuilder()
                                .setObjectType(resourceType)
                                .setObjectId(resourceId.toString()))
                        .setSubject(SubjectReference.newBuilder()
                                .setObject(ObjectReference.newBuilder()
                                        .setObjectType(USER)
                                        .setObjectId(subjectId.toString()))))
                .build();
    }

    private Iterable<LookupSubjectsResponse> userLookup(UUID oldGroupId) {
        LookupSubjectsRequest request = LookupSubjectsRequest.newBuilder()
                .setPermission(MEMBER)
                .setResource(ObjectReference.newBuilder()
                        .setObjectType(GROUP)
                        .setObjectId(oldGroupId.toString()))
                .build();
        try {
            Iterator<LookupSubjectsResponse> response = permissionsService.lookupSubjects(request);
            return () -> response;
        } catch (Exception e) {
            LOGGER.warn("Error while requesting users of group {}", oldGroupId, e);
            return List.of();
        }
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
