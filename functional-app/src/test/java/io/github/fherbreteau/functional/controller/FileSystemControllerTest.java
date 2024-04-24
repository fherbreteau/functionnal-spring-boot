package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.FunctionalApplication;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.Path;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.driving.FileService;
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

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FunctionalApplication.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
class FileSystemControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private FileService fileService;

    private MockMvc mvc;

    private File file;
    private Folder folder;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        file = File.builder()
                .withName("file")
                .withParent(Path.ROOT.getItemAsFolder())
                .withOwner(User.user("user"))
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readOnly())
                .withOtherAccess(AccessRight.none())
                .build();
        folder = Folder.builder()
                .withName("folder")
                .withParent(Path.ROOT.getItemAsFolder())
                .withOwner(User.user("user"))
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readOnly())
                .withOtherAccess(AccessRight.none())
                .build();
    }

    @WithMockUser
    @Test
    void shouldReturnAListOfItemWhenFileServiceCanListElement() throws Exception {
        when(fileService.getPath(eq("/"), argThat(user -> Objects.equals(user.getName(), "user"))))
                .thenReturn(Path.ROOT);
        when(fileService.processCommand(eq(CommandType.LIST),
                argThat(user -> Objects.equals(user.getName(), "user")),
                argThat(input -> Objects.equals(input.getItem(), Path.ROOT.getItem()))))
                .thenReturn(new Output(List.of()));

        mvc.perform(get("/")
                        .queryParam("path", "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

    }

    @WithMockUser
    @Test
    void shouldCreateFileWhenFileServiceCanCreateFile() throws Exception {
        when(fileService.getPath(eq("/"), argThat(user -> Objects.equals(user.getName(), "user"))))
                .thenReturn(Path.ROOT);
        when(fileService.processCommand(eq(CommandType.TOUCH),
                argThat(user -> Objects.equals(user.getName(), "user")),
                argThat(input -> Objects.equals(input.getItem(), Path.ROOT.getItem()) &&
                        Objects.equals(input.getName(), "file"))))
                .thenReturn(new Output(file));

        mvc.perform(post("/file").with(csrf())
                        .queryParam("path", "/")
                        .queryParam("name", "file"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("file"));

    }

    @WithMockUser
    @Test
    void shouldCreateFolderWhenFileServiceCanCreateFolder() throws Exception {
        when(fileService.getPath(eq("/"), argThat(user -> Objects.equals(user.getName(), "user"))))
                .thenReturn(Path.ROOT);
        when(fileService.processCommand(eq(CommandType.MKDIR),
                argThat(user -> Objects.equals(user.getName(), "user")),
                argThat(input -> Objects.equals(input.getItem(), Path.ROOT.getItem()) &&
                        Objects.equals(input.getName(), "folder"))))
                .thenReturn(new Output(folder));

        mvc.perform(post("/folder").with(csrf())
                        .queryParam("path", "/")
                        .queryParam("name", "folder"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("folder"));

    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenPathDoesNotExists() throws Exception {
        when(fileService.getPath(eq("/path"), argThat(user -> Objects.equals(user.getName(), "user"))))
                .thenReturn(Path.error(new Error("Error")));

        mvc.perform(post("/folder").with(csrf())
                        .queryParam("path", "/path")
                        .queryParam("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Error"));

    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenCommandFails() throws Exception {
        when(fileService.getPath(eq("/path"), argThat(user -> Objects.equals(user.getName(), "user"))))
                .thenReturn(Path.success("path", null));
        when(fileService.processCommand(eq(CommandType.MKDIR),
                argThat(user -> Objects.equals(user.getName(), "user")),
                argThat(input -> Objects.equals(input.getName(), "folder"))))
                .thenReturn(new Output(new Error("Error")));

        mvc.perform(post("/folder").with(csrf())
                        .queryParam("path", "/path")
                        .queryParam("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Error"));

    }
}
