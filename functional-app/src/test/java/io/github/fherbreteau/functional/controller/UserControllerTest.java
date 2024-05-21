package io.github.fherbreteau.functional.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fherbreteau.functional.FunctionalApplication;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;
import io.github.fherbreteau.functional.model.CreateUserDTO;
import io.github.fherbreteau.functional.model.ModifyUserDTO;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FunctionalApplication.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
class UserControllerTest {

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
    void shouldCreateUserWithGivenParameters() throws Exception {
        given(userChecker.canCreateUser("user1", actor)).willReturn(true);
        given(userRepository.exists("user1")).willReturn(false);
        given(groupRepository.exists("user1")).willReturn(false);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(groupRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        CreateUserDTO dto = CreateUserDTO.builder()
                .withName("user1")
                .build();
        mvc.perform(post("/users").with(csrf())
                        .content(mapper.writeValueAsBytes(dto))
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
    void shouldModifyUserWithGivenParameters() throws Exception {
        UUID groupId = UUID.randomUUID();
        Group group = Group.builder("group").withGroupId(groupId).build();
        User user = User.builder("user1").withGroup(Group.builder("user1").build()).build();
        given(userChecker.canUpdateUser("user1", actor)).willReturn(true);
        given(userRepository.exists("user1")).willReturn(true);
        given(userRepository.findByName("user1")).willReturn(user);
        given(groupRepository.exists(groupId)).willReturn(true);
        given(groupRepository.findById(groupId)).willReturn(group);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        ModifyUserDTO dto = ModifyUserDTO.builder()
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
        User user = User.builder("user1").withGroup(Group.builder("user1").build()).build();
        given(userChecker.canUpdateUser("user1", actor)).willReturn(true);
        given(userRepository.exists("user1")).willReturn(true);
        given(userRepository.findByName("user1")).willReturn(user);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(userRepository.updatePassword(eq(user), any())).willAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(put("/users/user1/password").with(csrf())
                        .content("Pa$sw0rd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("user1"))
                .andExpect(jsonPath("$.groups").isArray())
                .andExpect(jsonPath("$.groups", hasSize(1)))
                .andExpect(jsonPath("$.groups[0].name").value("user1"));

        then(userRepository).should().updatePassword(eq(user), passwordCaptor.capture());
        assertThat(passwordCaptor.getValue())
                .isNotEqualTo("Pa$sw0rd");
    }

    @WithMockUser
    @Test
    void shouldDeleteUser() throws Exception {
        User user = User.builder("user1").withGroup(Group.builder("user1").build()).build();
        given(userChecker.canDeleteUser("user1", actor)).willReturn(true);
        given(userRepository.exists("user1")).willReturn(true);
        given(userRepository.findByName("user1")).willReturn(user);
        given(userRepository.delete(any())).willAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(delete("/users/user1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("user1"))
                .andExpect(jsonPath("$.groups").isArray())
                .andExpect(jsonPath("$.groups", hasSize(1)))
                .andExpect(jsonPath("$.groups[0].name").value("user1"));
    }
}
