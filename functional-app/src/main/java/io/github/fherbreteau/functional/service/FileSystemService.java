package io.github.fherbreteau.functional.service;

import io.github.fherbreteau.functional.mapper.EntityMapper;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.driving.FileService;
import io.github.fherbreteau.functional.exception.CommandException;
import io.github.fherbreteau.functional.exception.PathException;
import io.github.fherbreteau.functional.model.ItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileSystemService {

    private final FileService fileService;

    private final EntityMapper entityMapper;

    public FileSystemService(FileService fileService, EntityMapper entityMapper) {
        this.fileService = fileService;
        this.entityMapper = entityMapper;
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
}
