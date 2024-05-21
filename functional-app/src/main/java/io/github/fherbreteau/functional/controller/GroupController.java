package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.model.GroupDTO;
import io.github.fherbreteau.functional.service.UserManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final UserManagementService userManagementService;

    public GroupController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO, Principal user) {
        GroupDTO response = userManagementService.createGroup(groupDTO, user.getName());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{name}")
    public ResponseEntity<GroupDTO> modifyGroup(@PathVariable("name") String name,
                                                @RequestBody GroupDTO groupDTO, Principal user) {
        GroupDTO response = userManagementService.modifyGroup(name, groupDTO, user.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<GroupDTO> deleteGroup(@PathVariable("name") String name,
                                                @RequestParam(value = "force", required = false, defaultValue = "false") boolean force,
                                                Principal user) {
        GroupDTO response = userManagementService.deleteGroup(name, force, user.getName());
        return ResponseEntity.ok(response);
    }
}
