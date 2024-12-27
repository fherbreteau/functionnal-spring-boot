package io.github.fherbreteau.functional.check;

import static com.authzed.api.v1.CheckPermissionResponse.Permissionship.PERMISSIONSHIP_HAS_PERMISSION;
import static io.github.fherbreteau.functional.Entities.*;
import static io.github.fherbreteau.functional.check.Permissions.*;
import static io.github.fherbreteau.functional.check.Permissions.DELETE_GROUP;

import com.authzed.api.v1.*;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserCheckerImpl implements UserChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCheckerImpl.class);

    private final PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService;

    public UserCheckerImpl(PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService) {
        this.permissionsService = permissionsService;
    }

    @Override
    public boolean canCreateUser(String name, User actor) {
        CheckPermissionRequest request = CheckPermissionRequest.newBuilder()
                .setPermission(CREATE_USER)
                .setResource(ObjectReference.newBuilder()
                        .setObjectId(name)
                        .setObjectType(USER_NAME))
                .setSubject(SubjectReference.newBuilder()
                        .setObject(ObjectReference.newBuilder()
                                .setObjectId(actor.getUserId().toString())
                                .setObjectType(USER)))
                .build();
        try {
            CheckPermissionResponse response = permissionsService.checkPermission(request);
            return response.getPermissionship() == PERMISSIONSHIP_HAS_PERMISSION;
        } catch (Exception e) {
            LOGGER.error("Error while checking create user permission on {} by {} ", name, actor, e);
            return false;
        }
    }

    @Override
    public boolean canUpdateUser(String name, User actor) {
        CheckPermissionRequest request = CheckPermissionRequest.newBuilder()
                .setPermission(UPDATE_USER)
                .setResource(ObjectReference.newBuilder()
                        .setObjectId(name)
                        .setObjectType(USER_NAME))
                .setSubject(SubjectReference.newBuilder()
                        .setObject(ObjectReference.newBuilder()
                                .setObjectId(actor.getUserId().toString())
                                .setObjectType(USER)))
                .build();
        try {
            CheckPermissionResponse response = permissionsService.checkPermission(request);
            return response.getPermissionship() == PERMISSIONSHIP_HAS_PERMISSION;
        } catch (Exception e) {
            LOGGER.error("Error while checking update user permission on {} by {} ", name, actor, e);
            return false;
        }
    }

    @Override
    public boolean canDeleteUser(String name, User actor) {
        CheckPermissionRequest request = CheckPermissionRequest.newBuilder()
                .setPermission(DELETE_USER)
                .setResource(ObjectReference.newBuilder()
                        .setObjectId(name)
                        .setObjectType(USER_NAME))
                .setSubject(SubjectReference.newBuilder()
                        .setObject(ObjectReference.newBuilder()
                                .setObjectId(actor.getUserId().toString())
                                .setObjectType(USER)))
                .build();
        try {
            CheckPermissionResponse response = permissionsService.checkPermission(request);
            return response.getPermissionship() == PERMISSIONSHIP_HAS_PERMISSION;
        } catch (Exception e) {
            LOGGER.error("Error while checking delete user permission on {} by {} ", name, actor, e);
            return false;
        }
    }

    @Override
    public boolean canCreateGroup(String name, User actor) {
        CheckPermissionRequest request = CheckPermissionRequest.newBuilder()
                .setPermission(CREATE_GROUP)
                .setResource(ObjectReference.newBuilder()
                        .setObjectId(name)
                        .setObjectType(GROUP_NAME))
                .setSubject(SubjectReference.newBuilder()
                        .setObject(ObjectReference.newBuilder()
                                .setObjectId(actor.getUserId().toString())
                                .setObjectType(USER)))
                .build();
        try {
            CheckPermissionResponse response = permissionsService.checkPermission(request);
            return response.getPermissionship() == PERMISSIONSHIP_HAS_PERMISSION;
        } catch (Exception e) {
            LOGGER.error("Error while checking create group permission on {} by {} ", name, actor, e);
            return false;
        }
    }

    @Override
    public boolean canUpdateGroup(String name, User actor) {
        CheckPermissionRequest request = CheckPermissionRequest.newBuilder()
                .setPermission(UPDATE_GROUP)
                .setResource(ObjectReference.newBuilder()
                        .setObjectId(name)
                        .setObjectType(GROUP_NAME))
                .setSubject(SubjectReference.newBuilder()
                        .setObject(ObjectReference.newBuilder()
                                .setObjectId(actor.getUserId().toString())
                                .setObjectType(USER)))
                .build();
        try {
            CheckPermissionResponse response = permissionsService.checkPermission(request);
            return response.getPermissionship() == PERMISSIONSHIP_HAS_PERMISSION;
        } catch (Exception e) {
            LOGGER.error("Error while checking update group permission on {} by {} ", name, actor, e);
            return false;
        }
    }

    @Override
    public boolean canDeleteGroup(String name, User actor) {
        CheckPermissionRequest request = CheckPermissionRequest.newBuilder()
                .setPermission(DELETE_GROUP)
                .setResource(ObjectReference.newBuilder()
                        .setObjectId(name)
                        .setObjectType(GROUP_NAME))
                .setSubject(SubjectReference.newBuilder()
                        .setObject(ObjectReference.newBuilder()
                                .setObjectId(actor.getUserId().toString())
                                .setObjectType(USER)))
                .build();
        try {
            CheckPermissionResponse response = permissionsService.checkPermission(request);
            return response.getPermissionship() == PERMISSIONSHIP_HAS_PERMISSION;
        } catch (Exception e) {
            LOGGER.error("Error while checking delete group permission on {} by {} ", name, actor, e);
            return false;
        }
    }
}
