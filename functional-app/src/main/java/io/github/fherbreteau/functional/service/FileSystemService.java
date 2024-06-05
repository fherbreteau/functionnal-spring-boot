package io.github.fherbreteau.functional.service;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driving.AccessParserService;
import io.github.fherbreteau.functional.driving.FileService;
import io.github.fherbreteau.functional.driving.UserService;
import io.github.fherbreteau.functional.exception.CommandException;
import io.github.fherbreteau.functional.exception.GroupException;
import io.github.fherbreteau.functional.exception.PathException;
import io.github.fherbreteau.functional.exception.UserException;
import io.github.fherbreteau.functional.mapper.EntityMapper;
import io.github.fherbreteau.functional.model.ItemDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class FileSystemService {

    private final FileService fileService;
    private final EntityMapper entityMapper;
    private final AccessParserService accessParserService;
    private final UserService userService;

    public FileSystemService(FileService fileService, EntityMapper entityMapper, AccessParserService accessParserService, UserService userService) {
        this.fileService = fileService;
        this.entityMapper = entityMapper;
        this.accessParserService = accessParserService;
        this.userService = userService;
    }

    public List<ItemDTO> listItems(String path, String username) {
        Output<User> userOutput = userService.findUserByName(username);
        if (userOutput.isError()) {
            throw new UserException(userOutput.getError());
        }
        User actor = userOutput.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).build();
        Output<List<Item>> itemsOutput = fileService.processCommand(ItemCommandType.LIST, actor, itemInput);
        if (itemsOutput.isError()) {
            throw new CommandException(itemsOutput.getError());
        }
        return entityMapper.mapToItemList(itemsOutput.getValue());
    }

    public ItemDTO createFile(String path, String name, String username) {
        Output<User> userOutput = userService.findUserByName(username);
        if (userOutput.isError()) {
            throw new UserException(userOutput.getError());
        }
        User actor = userOutput.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).withName(name).build();
        Output<Item> itemOutput = fileService.processCommand(ItemCommandType.TOUCH, actor, itemInput);
        if (itemOutput.isError()) {
            throw new CommandException(itemOutput.getError());
        }
        return entityMapper.mapToItem(itemOutput.getValue());

    }

    public ItemDTO createFolder(String path, String name, String username) {
        Output<User> userOutput = userService.findUserByName(username);
        if (userOutput.isError()) {
            throw new UserException(userOutput.getError());
        }
        User actor = userOutput.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).withName(name).build();
        Output<Item> itemOutput = fileService.processCommand(ItemCommandType.MKDIR, actor, itemInput);
        if (itemOutput.isError()) {
            throw new CommandException(itemOutput.getError());
        }
        return entityMapper.mapToItem(itemOutput.getValue());
    }

    public ItemDTO changeOwner(String path, String name, String username) {
        Output<User> userOutput = userService.findUserByName(username);
        if (userOutput.isError()) {
            throw new UserException(userOutput.getError());
        }
        User actor = userOutput.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        userOutput = userService.findUserByName(name);
        if (userOutput.isError()) {
            throw new UserException(userOutput.getError());
        }
        User owner = userOutput.getValue();
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).withUser(owner).build();
        Output<Item> itemOutput = fileService.processCommand(ItemCommandType.CHOWN, actor, itemInput);
        if (itemOutput.isError()) {
            throw new CommandException(itemOutput.getError());
        }
        return entityMapper.mapToItem(itemOutput.getValue());
    }

    public ItemDTO changeGroup(String path, String name, String username) {
        Output<User> userOutput = userService.findUserByName(username);
        if (userOutput.isError()) {
            throw new UserException(userOutput.getError());
        }
        User actor = userOutput.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        Output<Group> groupOutput = userService.findGroupByName(name);
        if (groupOutput.isError()) {
            throw new GroupException(groupOutput.getError());
        }
        Group group = groupOutput.getValue();
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).withGroup(group).build();
        Output<Item> itemOutput = fileService.processCommand(ItemCommandType.CHGRP, actor, itemInput);
        if (itemOutput.isError()) {
            throw new CommandException(itemOutput.getError());
        }
        return entityMapper.mapToItem(itemOutput.getValue());
    }

    public ItemDTO changeMode(String path, String right, String username) {
        Output<User> userOutput = userService.findUserByName(username);
        if (userOutput.isError()) {
            throw new UserException(userOutput.getError());
        }
        User actor = userOutput.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = accessParserService.parseAccessRights(right, itemPath.getItem());
        Output<Item> itemOutput = fileService.processCommand(ItemCommandType.CHMOD, actor, itemInput);
        if (itemOutput.isError()) {
            throw new CommandException(itemOutput.getError());
        }
        return entityMapper.mapToItem(itemOutput.getValue());
    }

    public ItemDTO upload(String path, MultipartFile file, String username) {
        try (InputStream content = file.getInputStream()) {
            Output<User> userOutput = userService.findUserByName(username);
            if (userOutput.isError()) {
                throw new UserException(userOutput.getError());
            }
            User actor = userOutput.getValue();
            Path itemPath = fileService.getPath(path, actor);
            if (itemPath.isError()) {
                throw new PathException(itemPath.getError());
            }
            String contentType = Optional.of(file)
                    .map(MultipartFile::getContentType)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            ItemInput itemInput = ItemInput.builder(itemPath.getItem())
                    .withContent(content)
                    .withContentType(contentType)
                    .build();
            Output<Item> itemOutput = fileService.processCommand(ItemCommandType.UPLOAD, actor, itemInput);
            if (itemOutput.isError()) {
                throw new CommandException(itemOutput.getError());
            }
            return entityMapper.mapToItem(itemOutput.getValue());
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }

    public ResponseEntity<InputStreamResource> download(String path, String username) {
        Output<User> userOutput = userService.findUserByName(username);
        if (userOutput.isError()) {
            throw new UserException(userOutput.getError());
        }
        User actor = userOutput.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem())
                .withContentType(itemPath.getContentType())
                .build();
        Output<InputStream> streamOutput = fileService.processCommand(ItemCommandType.DOWNLOAD, actor, itemInput);
        if (streamOutput.isError()) {
            throw new CommandException(streamOutput.getError());
        }
        return entityMapper.mapStream(streamOutput.getValue(), itemInput.getContentType());
    }

    public void delete(String path, String username) {
        Output<User> userOutput = userService.findUserByName(username);
        if (userOutput.isError()) {
            throw new UserException(userOutput.getError());
        }
        User actor = userOutput.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).build();
        Output<Void> itemOutput = fileService.processCommand(ItemCommandType.DELETE, actor, itemInput);
        if (itemOutput.isError()) {
            throw new CommandException(itemOutput.getError());
        }
    }
}
