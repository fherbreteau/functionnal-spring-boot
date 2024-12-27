package io.github.fherbreteau.functional.update;

import static com.authzed.api.v1.RelationshipUpdate.Operation.OPERATION_CREATE;
import static io.github.fherbreteau.functional.Entities.*;
import static io.github.fherbreteau.functional.update.Relations.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.api.v1.RelationshipUpdate;
import com.authzed.api.v1.WriteRelationshipsRequest;
import io.github.fherbreteau.functional.domain.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccessUpdaterTest {

    @Mock
    private PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService;

    private UUID fileHandle;

    private Item item;

    private UUID userId;

    private UUID groupId;

    @InjectMocks
    private AccessUpdaterImpl accessUpdater;

    @Captor
    private ArgumentCaptor<WriteRelationshipsRequest> requestCaptor;

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
        User owner = User.builder("user")
                .withUserId(userId)
                .build();
        groupId = UUID.randomUUID();
        Group group = Group.builder("group")
                .withGroupId(groupId)
                .build();

        fileHandle = UUID.randomUUID();
        item = File.builder()
                .withName("file")
                .withHandle(fileHandle)
                .withParent(Folder.getRoot())
                .withOwner(owner)
                .withGroup(group)
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readOnly())
                .withOtherAccess(AccessRight.none())
                .build();
    }

    @Test
    void shouldPublishToSpiceDbWhenCreatingItem() {
        // Arrange
        // Act
        Item result = accessUpdater.createItem(item);
        // Assert
        assertThat(result).isEqualTo(item);
        verify(permissionsService).writeRelationships(requestCaptor.capture());
        assertThat(requestCaptor.getValue())
                .extracting(WriteRelationshipsRequest::getUpdatesList, list(RelationshipUpdate.class))
                .hasSize(4)
                .extracting(RelationshipUpdate::getOperation, this::getRelation, this::getSubjectType, this::getSubjectId, this::getResourceType, this::getResourceId)
                .containsExactly(
                        tuple(OPERATION_CREATE, USER_READ, USER, userId.toString(), ITEM, fileHandle.toString()),
                        tuple(OPERATION_CREATE, USER_WRITE, USER, userId.toString(), ITEM, fileHandle.toString()),
                        tuple(OPERATION_CREATE, USER_EXECUTE, USER, userId.toString(), ITEM, fileHandle.toString()),
                        tuple(OPERATION_CREATE, GROUP_READ, GROUP, groupId.toString(), ITEM, fileHandle.toString()));
    }

    @Test
    void shouldHandleErrorFromSpiceDbWhenCreatingItem() {
        // Arrange
        when(permissionsService.writeRelationships(any()))
                .thenThrow(RuntimeException.class);
        // Act
        Item result = accessUpdater.createItem(item);
        // Assert
        assertThat(result).isEqualTo(item);
    }

    private String getRelation(RelationshipUpdate update) {
        return update.getRelationship().getRelation();
    }

    private String getSubjectType(RelationshipUpdate update) {
        return update.getRelationship().getSubject().getObject().getObjectType();
    }

    private String getSubjectId(RelationshipUpdate update) {
        return update.getRelationship().getSubject().getObject().getObjectId();
    }

    private String getResourceType(RelationshipUpdate update) {
        return update.getRelationship().getResource().getObjectType();
    }

    private String getResourceId(RelationshipUpdate update) {
        return update.getRelationship().getResource().getObjectId();
    }
}
