package io.github.fherbreteau.functional.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fherbreteau.functional.FunctionalApplication;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;
import io.github.fherbreteau.functional.exception.NotFoundException;
import io.github.fherbreteau.functional.model.GroupDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

import java.util.UUID;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FunctionalApplication.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
class GroupControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserChecker userChecker;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private GroupRepository groupRepository;

    private MockMvc mvc;

    private User actor;

    @Captor
    private ArgumentCaptor<String> passwordCaptor;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        actor = User.builder("user").build();
        given(userRepository.findByName("user")).willReturn(actor);
    }

    @WithMockUser
    @Test
    void shouldCreateGroupWithGivenParameters() throws Exception {
        given(userChecker.canCreateGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(false);
        given(groupRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        UUID groupId = UUID.randomUUID();
        GroupDTO dto = GroupDTO.builder()
                .withName("group")
                .withGid(groupId)
                .build();
        mvc.perform(post("/groups").with(csrf())
                        .content(mapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("group"))
                .andExpect(jsonPath("$.gid").value(groupId.toString()));
    }

    @WithMockUser
    @Test
    void shouldModifyGroupWithGivenParameters() throws Exception {
        UUID groupId = UUID.randomUUID();
        Group group = Group.builder("group").withGroupId(groupId).build();
        given(userChecker.canUpdateGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(groupRepository.findByName("group")).willReturn(group);
        given(groupRepository.exists(groupId)).willReturn(false);
        given(groupRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        GroupDTO dto = GroupDTO.builder()
                .withGid(groupId)
                .build();
        mvc.perform(patch("/groups/group").with(csrf())
                        .content(mapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("group"))
                .andExpect(jsonPath("$.gid").value(groupId.toString()));
    }

    @WithMockUser
    @Test
    void shouldDeleteGroup() throws Exception {
        UUID groupId = UUID.randomUUID();
        Group group = Group.builder("group").withGroupId(groupId).build();
        given(userChecker.canDeleteGroup("group", actor)).willReturn(true);
        given(groupRepository.exists("group")).willReturn(true);
        given(groupRepository.findByName("group")).willReturn(group);
        given(groupRepository.delete(any())).willAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(delete("/groups/group").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("group"))
                .andExpect(jsonPath("$.gid").value(groupId.toString()));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenConnectedUserDoesNotExists() throws Exception {
        given(userRepository.findByName("user")).willThrow(new NotFoundException("user"));

        GroupDTO dto = GroupDTO.builder()
                .withName("group")
                .build();
        mvc.perform(post("/groups").with(csrf())
                        .content(mapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(patch("/groups/group").with(csrf())
                        .content(mapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(delete("/groups/group").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenCommandFails() throws Exception {
        given(userRepository.exists("user1")).willReturn(true, false);
        GroupDTO dto = GroupDTO.builder()
                .withName("group")
                .build();
        mvc.perform(post("/groups").with(csrf())
                        .content(mapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("GROUPADD with arguments UserInput{userId=null, name='group', password='null', groupId=null, groups='[]', newName='null', force=false, append=false} failed for ")));

        mvc.perform(patch("/groups/group").with(csrf())
                        .content(mapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("GROUPMOD with arguments UserInput{userId=null, name='group', password='null', groupId=null, groups='[]', newName='group', force=false, append=false} failed for ")));

        mvc.perform(delete("/groups/group").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("GROUPDEL with arguments UserInput{userId=null, name='group', password='null', groupId=null, groups='[]', newName='null', force=false, append=false} failed for ")));
    }
}
