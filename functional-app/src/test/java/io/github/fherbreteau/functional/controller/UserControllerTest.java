package io.github.fherbreteau.functional.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fherbreteau.functional.FunctionalApplication;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driving.UserService;
import io.github.fherbreteau.functional.model.InputUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FunctionalApplication.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService userService;

    private MockMvc mvc;

    private User actor;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        actor = User.builder("user").build();
        given(userService.findUserByName("user")).willReturn(Output.success(actor));
    }

    @WithMockUser
    @Test
    void shouldReturnCurrentUserWithGivenName() throws Exception {
        given(userService.processCommand(eq(UserCommandType.ID), eq(actor),
                argThat(argument -> Objects.isNull(argument.getName()) && Objects.isNull(argument.getUserId()))))
                .willReturn(Output.success(actor));
        mvc.perform(get("/users").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uid").value(actor.getUserId().toString()))
                .andExpect(jsonPath("$.name").value("user"))
                .andExpect(jsonPath("$.groups").isArray())
                .andExpect(jsonPath("$.groups", hasSize(1)))
                .andExpect(jsonPath("$.groups[0].gid").value(actor.getUserId().toString()))
                .andExpect(jsonPath("$.groups[0].name").value("user"));
    }

    @WithMockUser
    @Test
    void shouldCreateUserWithGivenParameters() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        given(userService.processCommand(eq(UserCommandType.USERADD), eq(actor),
                argThat(argument -> Objects.equals(argument.getName(), "user1")
                && Objects.equals(argument.getUserId(), userId)
                && Objects.equals(argument.getGroupId(), groupId)
                && Objects.equals(argument.getPassword(), "Password")
                && isEqualCollection(argument.getGroups(), List.of("group1", "group2")))))
                .willReturn(Output.success(User.builder("user1").withUserId(userId).build()));

        InputUserDTO dto = InputUserDTO.builder()
                .withName("user1")
                .withUid(userId)
                .withGid(groupId)
                .withPassword("Password")
                .withGroups(List.of("group1", "group2"))
                .build();
        mvc.perform(post("/users").with(csrf())
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uid").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("user1"))
                .andExpect(jsonPath("$.groups").isArray())
                .andExpect(jsonPath("$.groups", hasSize(1)))
                .andExpect(jsonPath("$.groups[0].gid").value(userId.toString()))
                .andExpect(jsonPath("$.groups[0].name").value("user1"));
    }

    @WithMockUser
    @Test
    void shouldModifyUserWithGivenParameters() throws Exception {
        UUID groupId = UUID.randomUUID();
        Group group = Group.builder("group").withGroupId(groupId).build();
        given(userService.processCommand(eq(UserCommandType.USERMOD), eq(actor),
                argThat(argument -> Objects.equals(argument.getName(), "user1") &&
                        Objects.equals(argument.getGroupId(), groupId))))
                .willReturn(Output.success(User.builder("user1").withGroup(group).build()));

        InputUserDTO dto = InputUserDTO.builder()
                .withGid(groupId)
                .build();
        mvc.perform(patch("/users/user1").with(csrf())
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("user1"))
                .andExpect(jsonPath("$.groups").isArray())
                .andExpect(jsonPath("$.groups", hasSize(1)))
                .andExpect(jsonPath("$.groups[0].name").value("group"));
    }

    @WithMockUser
    @Test
    void shouldModifyUserPassword() throws Exception {
        given(userService.processCommand(eq(UserCommandType.PASSWD), eq(actor),
                argThat(argument -> Objects.equals(argument.getName(), "user1") &&
                        Objects.equals(argument.getPassword(), "Pa$sw0rd"))))
                .willReturn(Output.success(User.builder("user1").build()));

        mvc.perform(put("/users/user1/password").with(csrf())
                .content("Pa$sw0rd")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("user1"))
                .andExpect(jsonPath("$.groups").isArray())
                .andExpect(jsonPath("$.groups", hasSize(1)))
                .andExpect(jsonPath("$.groups[0].name").value("user1"));
    }

    @WithMockUser
    @Test
    void shouldDeleteUser() throws Exception {
        given(userService.processCommand(eq(UserCommandType.USERDEL), eq(actor),
                argThat(argument -> Objects.equals(argument.getName(), "user1"))))
                .willReturn(Output.success(User.builder("user1").build()));

        mvc.perform(delete("/users/user1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("user1"))
                .andExpect(jsonPath("$.groups").isArray())
                .andExpect(jsonPath("$.groups", hasSize(1)))
                .andExpect(jsonPath("$.groups[0].name").value("user1"));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenConnectedUserDoesNotExists() throws Exception {
        given(userService.findUserByName("user")).willReturn(Output.error("user not found"));

        mvc.perform(get("/users").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        InputUserDTO dto = InputUserDTO.builder().withName("user1").build();
        mvc.perform(post("/users").with(csrf())
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(patch("/users/user1").with(csrf())
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(put("/users/user1/password").with(csrf())
                .content("Pa$sw0rd")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(delete("/users/user1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenCommandFails() throws Exception {
        given(userService.processCommand(any(), eq(actor), any()))
                .willReturn(Output.error("Command Failed"));

        mvc.perform(get("/users").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"));

        InputUserDTO dto = InputUserDTO.builder().withName("user1").build();
        mvc.perform(post("/users").with(csrf())
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"));

        mvc.perform(patch("/users/user1").with(csrf())
                .content(mapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"));

        mvc.perform(put("/users/user1/password").with(csrf())
                .content("Pa$sw0rd")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"));

        mvc.perform(delete("/users/user1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"));
    }
}
