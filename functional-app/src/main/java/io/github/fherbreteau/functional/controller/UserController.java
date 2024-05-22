package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.model.InputUserDTO;
import io.github.fherbreteau.functional.model.UserDTO;
import io.github.fherbreteau.functional.service.UserManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserManagementService userManagementService;

    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUser(@RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "uid", required = false) UUID userId,
                                           Principal user) {
        UserDTO response = userManagementService.getUser(name, userId, user.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody InputUserDTO userDTO, Principal user) {
        UserDTO response = userManagementService.createUser(userDTO, user.getName());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{name}")
    public ResponseEntity<UserDTO> modifyUser(@PathVariable("name") String name,
                                              @RequestParam(value = "append", required = false, defaultValue = "false") boolean append,
                                              @RequestBody InputUserDTO userDTO, Principal user) {
        UserDTO response = userManagementService.modifyUser(name, userDTO, append, user.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{name}/password")
    public ResponseEntity<UserDTO> updatePassword(@PathVariable("name") String name, @RequestBody String password, Principal user) {
        UserDTO response = userManagementService.updatePassword(name, password, user.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("name") String name, Principal user) {
        UserDTO response = userManagementService.deleteUser(name, user.getName());
        return ResponseEntity.ok(response);
    }
}
