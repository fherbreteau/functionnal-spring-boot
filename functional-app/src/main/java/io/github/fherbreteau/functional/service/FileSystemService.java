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
        Output output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = (User) output.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).build();
        output = fileService.processCommand(ItemCommandType.LIST, actor, itemInput);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToItemList(output.getValue());
    }

    public ItemDTO createFile(String path, String name, String username) {
        Output output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = (User) output.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).withName(name).build();
        output = fileService.processCommand(ItemCommandType.TOUCH, actor, itemInput);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToItem(output.getValue());

    }

    public ItemDTO createFolder(String path, String name, String username) {
        Output output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = (User) output.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).withName(name).build();
        output = fileService.processCommand(ItemCommandType.MKDIR, actor, itemInput);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToItem(output.getValue());
    }

    public ItemDTO changeOwner(String path, String name, String username) {
        Output output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = (User) output.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        output = userService.findUserByName(name);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User owner = (User) output.getValue();
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).withUser(owner).build();
        output = fileService.processCommand(ItemCommandType.CHOWN, actor, itemInput);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToItem(output.getValue());
    }

    public ItemDTO changeGroup(String path, String name, String username) {
        Output output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = (User) output.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        output = userService.findGroupByName(name);
        if (output.isError()) {
            throw new GroupException(output.getError());
        }
        Group group = (Group) output.getValue();
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).withGroup(group).build();
        output = fileService.processCommand(ItemCommandType.CHGRP, actor, itemInput);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToItem(output.getValue());
    }

    public ItemDTO changeMode(String path, String right, String username) {
        Output output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = (User) output.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = accessParserService.parseAccessRights(right, itemPath.getItem());
        output = fileService.processCommand(ItemCommandType.CHMOD, actor, itemInput);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToItem(output.getValue());
    }

    public ItemDTO upload(String path, MultipartFile file, String username) {
        try (InputStream content = file.getInputStream()) {
            Output output = userService.findUserByName(username);
            if (output.isError()) {
                throw new UserException(output.getError());
            }
            User actor = (User) output.getValue();
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
            output = fileService.processCommand(ItemCommandType.UPLOAD, actor, itemInput);
            if (output.isError()) {
                throw new CommandException(output.getError());
            }
            return entityMapper.mapToItem(output.getValue());
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }

    public ResponseEntity<InputStreamResource> download(String path, String username) {
        Output output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = (User) output.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem())
                .withContentType(itemPath.getContentType())
                .build();
        output = fileService.processCommand(ItemCommandType.DOWNLOAD, actor, itemInput);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapStream(output.getValue(), itemInput.getContentType());
    }

    public ItemDTO delete(String path, String username) {
        Output output = userService.findUserByName(username);
        if (output.isError()) {
            throw new UserException(output.getError());
        }
        User actor = (User) output.getValue();
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        ItemInput itemInput = ItemInput.builder(itemPath.getItem()).build();
        output = fileService.processCommand(ItemCommandType.DELETE, actor, itemInput);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToItem(output.getValue());
    }
}
