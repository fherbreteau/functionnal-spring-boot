package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.model.ItemDTO;
import io.github.fherbreteau.functional.service.FileSystemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class FileSystemController {

    private final FileSystemService fileSystemService;

    public FileSystemController(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> listItems(@RequestParam("path") String path, AuthenticatedPrincipal user) {
        return ResponseEntity.ok(fileSystemService.listItems(path, user.getName()));
    }

    @PostMapping(path = "/file")
    public ResponseEntity<ItemDTO> createFile(@RequestParam("path") String path,
                                              @RequestParam("name") String name,
                                              AuthenticatedPrincipal user) {
        return ResponseEntity.ok(fileSystemService.createFile(path, name, user.getName()));
    }

    @PostMapping(path = "/folder")
    public ResponseEntity<ItemDTO> createFolder(@RequestParam("path") String path,
                                              @RequestParam("name") String name,
                                              AuthenticatedPrincipal user) {
        return ResponseEntity.ok(fileSystemService.createFolder(path, name, user.getName()));
    }
}
