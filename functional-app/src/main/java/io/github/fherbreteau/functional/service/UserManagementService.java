package io.github.fherbreteau.functional.service;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driving.UserService;
import io.github.fherbreteau.functional.exception.CommandException;
import io.github.fherbreteau.functional.exception.UserException;
import io.github.fherbreteau.functional.mapper.EntityMapper;
import io.github.fherbreteau.functional.model.InputUserDTO;
import io.github.fherbreteau.functional.model.GroupDTO;
import io.github.fherbreteau.functional.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserManagementService {

    private final UserService userService;
    private final EntityMapper entityMapper;

    public UserManagementService(UserService userService, EntityMapper entityMapper) {
        this.userService = userService;
        this.entityMapper = entityMapper;
    }

    public UserDTO getUser(String name, UUID userId, String username) {
        Output<User> output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = output.getValue();
        UserInput input = UserInput.builder(name)
                .withUserId(userId)
                .build();
        output = userService.processCommand(UserCommandType.ID, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToUser(output.getValue());
    }

    public UserDTO createUser(InputUserDTO userDTO, String username) {
        Output<User> output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = output.getValue();
        UserInput input = UserInput.builder(userDTO.getName())
                .withUserId(userDTO.getUid())
                .withGroupId(userDTO.getGid())
                .withGroups(userDTO.getGroups())
                .withPassword(userDTO.getPassword())
                .build();
        output = userService.processCommand(UserCommandType.USERADD, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToUser(output.getValue());
    }

    public UserDTO modifyUser(String name, InputUserDTO userDTO, boolean append, String username) {
        Output<User> output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = output.getValue();
        UserInput input = UserInput.builder(name)
                .withUserId(userDTO.getUid())
                .withGroupId(userDTO.getGid())
                .withGroups(userDTO.getGroups())
                .withPassword(userDTO.getPassword())
                .withNewName(userDTO.getName())
                .withAppend(append)
                .build();
        output = userService.processCommand(UserCommandType.USERMOD, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToUser(output.getValue());
    }

    public UserDTO updatePassword(String name, String password, String username) {
        Output<User> output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = output.getValue();
        UserInput input = UserInput.builder(name)
                .withPassword(password)
                .build();
        output = userService.processCommand(UserCommandType.PASSWD, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToUser(output.getValue());
    }

    public void deleteUser(String name, String username) {
        Output<User> output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = output.getValue();
        UserInput input = UserInput.builder(name)
                .build();
        output = userService.processCommand(UserCommandType.USERDEL, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
    }

    public List<GroupDTO> getGroups(String name, UUID userId, String username) {
        Output<User> output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = output.getValue();
        UserInput input = UserInput.builder(name)
                .withUserId(userId)
                .build();
        Output<Group> groupOutput = userService.processCommand(UserCommandType.GROUPS, actor, input);
        if (groupOutput.isError()) {
            throw new CommandException(groupOutput.getError());
        }
        return entityMapper.mapToGroupList(groupOutput.getValue());
    }

    public GroupDTO createGroup(GroupDTO groupDTO, String username) {
        Output<User> output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = output.getValue();
        UserInput input = UserInput.builder(groupDTO.getName())
                .withGroupId(groupDTO.getGid())
                .build();
        Output<Group> groupOutput = userService.processCommand(UserCommandType.GROUPADD, actor, input);
        if (groupOutput.isError()) {
            throw new CommandException(groupOutput.getError());
        }
        return entityMapper.mapToGroup(groupOutput.getValue());
    }

    public GroupDTO modifyGroup(String name, GroupDTO groupDTO, String username) {
        Output<User> output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = output.getValue();
        UserInput input = UserInput.builder(name)
                .withGroupId(groupDTO.getGid())
                .withNewName(groupDTO.getName())
                .build();
        Output<Group> groupOutput = userService.processCommand(UserCommandType.GROUPMOD, actor, input);
        if (groupOutput.isError()) {
            throw new CommandException(groupOutput.getError());
        }
        return entityMapper.mapToGroup(groupOutput.getValue());
    }

    public void deleteGroup(String name, boolean force, String username) {
        Output<User> output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = output.getValue();
        UserInput input = UserInput.builder(name)
                .withForce(force)
                .build();
        Output<Group> groupOutput = userService.processCommand(UserCommandType.GROUPDEL, actor, input);
        if (groupOutput.isError()) {
            throw new CommandException(groupOutput.getError());
        }
    }
}
