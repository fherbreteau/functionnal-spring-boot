package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.model.ItemDTO;
import io.github.fherbreteau.functional.service.FileSystemService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileSystemController {

    private final FileSystemService fileSystemService;

    public FileSystemController(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> listItems(@RequestParam("path") String path, Principal user) {
        return ResponseEntity.ok(fileSystemService.listItems(path, user.getName()));
    }

    @PostMapping(path = "/file")
    public ResponseEntity<ItemDTO> createFile(@RequestParam("path") String path,
                                              @RequestParam("name") String name,
                                              Principal user) {
        return ResponseEntity.ok(fileSystemService.createFile(path, name, user.getName()));
    }

    @PostMapping(path = "/folder")
    public ResponseEntity<ItemDTO> createFolder(@RequestParam("path") String path,
                                                @RequestParam("name") String name,
                                                Principal user) {
        return ResponseEntity.ok(fileSystemService.createFolder(path, name, user.getName()));
    }

    @PatchMapping(path = "/owner")
    public ResponseEntity<ItemDTO> changeOwner(@RequestParam("path") String path,
                                               @RequestParam("name") String name,
                                               Principal user) {
        return ResponseEntity.ok(fileSystemService.changeOwner(path, name, user.getName()));
    }

    @PatchMapping(path = "/group")
    public ResponseEntity<ItemDTO> changeGroup(@RequestParam("path") String path,
                                               @RequestParam("name") String name,
                                               Principal user) {
        return ResponseEntity.ok(fileSystemService.changeGroup(path, name, user.getName()));
    }

    @PatchMapping(path = "/mode")
    public ResponseEntity<ItemDTO> changeMode(@RequestParam("path") String path,
                                              @RequestParam("right") String right,
                                              Principal user) {
        return ResponseEntity.ok(fileSystemService.changeMode(path, right, user.getName()));
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<ItemDTO> upload(@RequestParam("path") String path,
                                          @RequestParam("file") MultipartFile file,
                                          Principal user) {
        return ResponseEntity.ok(fileSystemService.upload(path, file, user.getName()));
    }

    @GetMapping(path = "/download")
    public ResponseEntity<InputStreamResource> download(@RequestParam("path") String path,
                                                        Principal user) {
        return fileSystemService.download(path, user.getName());
    }
}
