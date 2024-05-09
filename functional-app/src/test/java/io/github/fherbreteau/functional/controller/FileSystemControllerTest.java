package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.FunctionalApplication;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
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
    private FileRepository fileRepository;
    @MockBean
    private AccessChecker accessChecker;

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
                .withParent(Folder.getRoot())
                .withOwner(User.user("user"))
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readOnly())
                .withOtherAccess(AccessRight.none())
                .build();
        folder = Folder.builder()
                .withName("folder")
                .withParent(Folder.getRoot())
                .withOwner(User.user("user"))
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readOnly())
                .withOtherAccess(AccessRight.none())
                .build();
    }

    @WithMockUser
    @Test
    void shouldReturnAListOfItemWhenFileServiceCanListElement() throws Exception {
        given(accessChecker.canRead(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByParentAndUser(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(List.of(file, folder));
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser(eq("folder"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(folder);
        given(accessChecker.canRead(eq(folder), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByParentAndUser(eq(folder), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(List.of());

        mvc.perform(get("/")
                        .queryParam("path", "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("file"))
                .andExpect(jsonPath("$[1].name").value("folder"));

        mvc.perform(get("/")
                        .queryParam("path", "/folder"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

    }

    @WithMockUser
    @Test
    void shouldCreateFileWhenFileServiceCanCreateFile() throws Exception {
        given(accessChecker.canWrite(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willReturn(file);

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
        given(accessChecker.canWrite(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willReturn(folder);

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
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser(eq("path"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(null);

        mvc.perform(post("/folder").with(csrf())
                        .queryParam("path", "/path")
                        .queryParam("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("path not found in ' null:null ------rwx null' for user"));

        mvc.perform(post("/file").with(csrf())
                        .queryParam("path", "/path")
                        .queryParam("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("path not found in ' null:null ------rwx null' for user"));

        mvc.perform(get("/")
                        .queryParam("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("path not found in ' null:null ------rwx null' for user"));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenCommandFails() throws Exception {
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        Folder path = Folder.builder().withName("path").withParent(Folder.getRoot()).build();
        given(fileRepository.findByNameAndParentAndUser(eq("path"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(path);
        given(accessChecker.canWrite(eq(folder), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(false);
        given(accessChecker.canRead(eq(folder), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(false);

        mvc.perform(post("/folder").with(csrf())
                        .queryParam("path", "/path")
                        .queryParam("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("MKDIR with arguments Input{item='path null:null --------- ', name='folder', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, content=<redacted>} failed for user"));

        mvc.perform(post("/file").with(csrf())
                        .queryParam("path", "/path")
                        .queryParam("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("TOUCH with arguments Input{item='path null:null --------- ', name='file', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, content=<redacted>} failed for user"));

        mvc.perform(get("/")
                        .queryParam("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("LIST with arguments Input{item='path null:null --------- ', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, content=<redacted>} failed for user"));
    }
}
