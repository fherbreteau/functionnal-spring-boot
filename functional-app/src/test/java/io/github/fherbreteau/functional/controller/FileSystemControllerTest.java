package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.FunctionalApplication;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.*;
import io.github.fherbreteau.functional.exception.NotFoundException;
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

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @MockBean
    private ContentRepository contentRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private GroupRepository groupRepository;

    private MockMvc mvc;

    private File file;
    private Folder folder;
    private LocalDateTime now;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        now = LocalDateTime.now();
        file = File.builder()
                .withName("file")
                .withParent(Folder.getRoot())
                .withOwner(User.builder("user").build())
                .withCreated(now)
                .withLastModified(now)
                .withLastAccessed(now)
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readOnly())
                .withOtherAccess(AccessRight.none())
                .withContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .build();
        folder = Folder.builder()
                .withName("folder")
                .withParent(Folder.getRoot())
                .withOwner(User.builder("user").build())
                .withCreated(now)
                .withLastModified(now)
                .withLastAccessed(now)
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readOnly())
                .withOtherAccess(AccessRight.none())
                .build();
        given(userRepository.findByName("user")).willReturn(User.builder("user").build());
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
                        .param("path", "/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("file"))
                .andExpect(jsonPath("$[0].created").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[0].modified").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[0].accessed").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[1].name").value("folder"))
                .andExpect(jsonPath("$[1].created").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[1].modified").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[1].accessed").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        mvc.perform(get("/")
                        .param("path", "/folder"))
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
                        .param("path", "/")
                        .param("name", "file"))
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
                        .param("path", "/")
                        .param("name", "folder"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("folder"));

    }

    @WithMockUser
    @Test
    void shouldChangeOwnerWhenFileServiceCanChangeOwner() throws Exception {
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser(eq("folder"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(folder);
        given(accessChecker.canChangeOwner(eq(folder), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(userRepository.findByName("user2")).willReturn(User.builder("user2").build());

        mvc.perform(patch("/owner").with(csrf())
                        .param("path", "/folder")
                        .param("name", "user2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.owner").value("user2"));
    }

    @WithMockUser
    @Test
    void shouldChangeGroupWhenFileServiceCanChangeGroup() throws Exception {
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser(eq("folder"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(folder);
        given(accessChecker.canChangeGroup(eq(folder), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(groupRepository.findByName("group2")).willReturn(Group.builder("group2").build());

        mvc.perform(patch("/group").with(csrf())
                        .param("path", "/folder")
                        .param("name", "group2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.group").value("group2"));
    }

    @WithMockUser
    @Test
    void shouldChangeRightWhenFileServiceCanChangeMode() throws Exception {
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser(eq("folder"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(folder);
        given(accessChecker.canChangeMode(eq(folder), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(patch("/mode").with(csrf())
                        .param("path", "/folder")
                        .param("right", "-wx"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access").value("dr--r-----"));
    }

    @WithMockUser
    @Test
    void shouldDownloadContentWhenFileServiceCanReadFile() throws Exception {
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser(eq("file"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(file);
        given(accessChecker.canRead(eq(file), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(contentRepository.readContent(file))
                .willReturn(new ByteArrayInputStream("content".getBytes()));

        mvc.perform(get("/download").with(csrf())
                        .param("path", "/file"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(content().bytes("content".getBytes()));
    }

    @WithMockUser
    @Test
    void shouldUploadContentWhenFileServiceCanWriteFile() throws Exception {
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser(eq("file"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(file);
        given(accessChecker.canWrite(eq(file), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(contentRepository.readContent(file))
                .willReturn(new ByteArrayInputStream("content".getBytes()));
        given(fileRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(multipart("/upload")
                        .file("file", "content".getBytes())
                        .with(csrf())
                        .param("path", "/file"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content-type").value(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenUserDoesNotExists() throws Exception {
        given(userRepository.findByName("user")).willThrow(new NotFoundException("user"));

        mvc.perform(post("/folder").with(csrf())
                        .param("path", "/path")
                        .param("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(post("/file").with(csrf())
                        .param("path", "/path")
                        .param("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(get("/")
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(patch("/owner").with(csrf())
                        .param("path", "/path")
                        .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(patch("/group").with(csrf())
                        .param("path", "/path")
                        .param("name", "group2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(patch("/mode").with(csrf())
                        .param("path", "/path")
                        .param("right", "-wx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(get("/download").with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(multipart("/upload")
                        .file("file", "content".getBytes())
                        .with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenPathDoesNotExists() throws Exception {
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser(eq("path"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(null);

        mvc.perform(post("/folder").with(csrf())
                        .param("path", "/path")
                        .param("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(post("/file").with(csrf())
                        .param("path", "/path")
                        .param("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(get("/")
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(patch("/owner").with(csrf())
                .param("path", "/path")
                .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(patch("/group").with(csrf())
                .param("path", "/path")
                .param("name", "group2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(patch("/mode").with(csrf())
                .param("path", "/path")
                .param("right", "-wx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(get("/download").with(csrf())
                .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(multipart("/upload")
                .file("file", "content".getBytes())
                .with(csrf())
                .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenParamDoesNotExists() throws Exception {
        given(accessChecker.canExecute(eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(true);
        Folder path = Folder.builder().withName("path").withParent(Folder.getRoot()).build();
        given(fileRepository.findByNameAndParentAndUser(eq("path"), eq(Folder.getRoot()), argThat(user -> Objects.equals(user.getName(), "user"))))
                .willReturn(path);
        given(userRepository.findByName("user2")).willThrow(new NotFoundException("user2"));
        given(groupRepository.findByName("group2")).willThrow(new NotFoundException("group2"));

        mvc.perform(patch("/owner").with(csrf())
                        .param("path", "/path")
                        .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user2 not found"));

        mvc.perform(patch("/group").with(csrf())
                        .param("path", "/path")
                        .param("name", "group2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("GroupException"))
                .andExpect(jsonPath("$.message").value("group2 not found"));

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
        given(userRepository.findByName("user2")).willReturn(User.builder("user2").build());
        given(groupRepository.findByName("group2")).willReturn(Group.builder("group2").build());

        mvc.perform(post("/folder").with(csrf())
                        .param("path", "/path")
                        .param("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("MKDIR with arguments Input{item='path null:null --------- ', name='folder', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(post("/file").with(csrf())
                        .param("path", "/path")
                        .param("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("TOUCH with arguments Input{item='path null:null --------- ', name='file', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(get("/")
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("LIST with arguments Input{item='path null:null --------- ', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(patch("/owner").with(csrf())
                        .param("path", "/path")
                        .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("CHOWN with arguments Input{item='path null:null --------- ', name='null', user=user2, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(patch("/group").with(csrf())
                        .param("path", "/path")
                        .param("name", "group2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("CHGRP with arguments Input{item='path null:null --------- ', name='null', user=null, group=group2, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(patch("/mode").with(csrf())
                        .param("path", "/path")
                        .param("right", "-wx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("CHMOD with arguments Input{item='path null:null --------- ', name='null', user=null, group=null, ownerAccess=---, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(get("/download").with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("DOWNLOAD with arguments Input{item='path null:null --------- ', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(multipart("/upload")
                        .file("file", "content".getBytes())
                        .with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("UPLOAD with arguments Input{item='path null:null --------- ', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=application/octet-stream} failed for user")));
    }
}
