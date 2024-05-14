package io.github.fherbreteau.functional.service;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driving.AccessParserService;
import io.github.fherbreteau.functional.driving.FileService;
import io.github.fherbreteau.functional.exception.CommandException;
import io.github.fherbreteau.functional.exception.PathException;
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

    public FileSystemService(FileService fileService, EntityMapper entityMapper, AccessParserService accessParserService) {
        this.fileService = fileService;
        this.entityMapper = entityMapper;
        this.accessParserService = accessParserService;
    }

    public List<ItemDTO> listItems(String path, String username) {
        User actor = User.user(username);
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        Input input = Input.builder(itemPath.getItem()).build();
        Output output = fileService.processCommand(CommandType.LIST, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapToList(output.getValue());
    }

    public ItemDTO createFile(String path, String name, String username) {
        User actor = User.user(username);
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        Input input = Input.builder(itemPath.getItem()).withName(name).build();
        Output output = fileService.processCommand(CommandType.TOUCH, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.map(output.getValue());

    }

    public ItemDTO createFolder(String path, String name, String username) {
        User actor = User.user(username);
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        Input input = Input.builder(itemPath.getItem()).withName(name).build();
        Output output = fileService.processCommand(CommandType.MKDIR, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.map(output.getValue());
    }

    public ItemDTO changeOwner(String path, String name, String username) {
        User actor = User.user(username);
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        User owner = User.user(name);
        Input input = Input.builder(itemPath.getItem()).withUser(owner).build();
        Output output = fileService.processCommand(CommandType.CHOWN, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.map(output.getValue());
    }

    public ItemDTO changeGroup(String path, String name, String username) {
        User actor = User.user(username);
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        Group group = Group.group(name);
        Input input = Input.builder(itemPath.getItem()).withGroup(group).build();
        Output output = fileService.processCommand(CommandType.CHGRP, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.map(output.getValue());
    }

    public ItemDTO changeMode(String path, String right, String username) {
        User actor = User.user(username);
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        Input input = accessParserService.parseAccessRights(right, itemPath.getItem());
        Output output = fileService.processCommand(CommandType.CHMOD, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.map(output.getValue());
    }

    public ItemDTO upload(String path, MultipartFile file, String username) {
        try (InputStream content = file.getInputStream()) {
            User actor = User.user(username);
            Path itemPath = fileService.getPath(path, actor);
            if (itemPath.isError()) {
                throw new PathException(itemPath.getError());
            }
            String contentType = Optional.of(file)
                    .map(MultipartFile::getContentType)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            Input input = Input.builder(itemPath.getItem())
                    .withContent(content)
                    .withContentType(contentType)
                    .build();
            Output output = fileService.processCommand(CommandType.UPLOAD, actor, input);
            if (output.isError()) {
                throw new CommandException(output.getError());
            }
            return entityMapper.map(output.getValue());
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }

    public ResponseEntity<InputStreamResource> download(String path, String username) {
        User actor = User.user(username);
        Path itemPath = fileService.getPath(path, actor);
        if (itemPath.isError()) {
            throw new PathException(itemPath.getError());
        }
        Input input = Input.builder(itemPath.getItem())
                .withContentType(itemPath.getContentType())
                .build();
        Output output = fileService.processCommand(CommandType.DOWNLOAD, actor, input);
        if (output.isError()) {
            throw new CommandException(output.getError());
        }
        return entityMapper.mapStream(output.getValue(), input.getContentType());
    }
}
