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

import static org.hamcrest.Matchers.hasSize;
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
    private User actor;

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
        actor = User.builder("user").build();
        given(userRepository.findByName("user")).willReturn(actor);
    }

    @WithMockUser
    @Test
    void shouldReturnAListOfItemWhenFileServiceCanListElement() throws Exception {
        given(accessChecker.canRead(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.findByParentAndUser(Folder.getRoot(), actor))
                .willReturn(List.of(file, folder));
        given(accessChecker.canExecute(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser("folder", Folder.getRoot(), actor))
                .willReturn(folder);
        given(accessChecker.canRead(folder, actor))
                .willReturn(true);
        given(fileRepository.findByParentAndUser(folder, actor))
                .willReturn(List.of());

        mvc.perform(get("/files")
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

        mvc.perform(get("/files")
                        .param("path", "/folder"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

    }

    @WithMockUser
    @Test
    void shouldCreateFileWhenFileServiceCanCreateFile() throws Exception {
        given(accessChecker.canWrite(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willReturn(file);

        mvc.perform(post("/files/file").with(csrf())
                        .param("path", "/")
                        .param("name", "file"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("file"));

    }

    @WithMockUser
    @Test
    void shouldCreateFolderWhenFileServiceCanCreateFolder() throws Exception {
        given(accessChecker.canWrite(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willReturn(folder);

        mvc.perform(post("/files/folder").with(csrf())
                        .param("path", "/")
                        .param("name", "folder"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("folder"));

    }

    @WithMockUser
    @Test
    void shouldChangeOwnerWhenFileServiceCanChangeOwner() throws Exception {
        given(accessChecker.canExecute(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser("folder", Folder.getRoot(), actor))
                .willReturn(folder);
        given(accessChecker.canChangeOwner(folder, actor))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(userRepository.findByName("user2")).willReturn(User.builder("user2").build());

        mvc.perform(patch("/files/owner").with(csrf())
                        .param("path", "/folder")
                        .param("name", "user2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.owner").value("user2"));
    }

    @WithMockUser
    @Test
    void shouldChangeGroupWhenFileServiceCanChangeGroup() throws Exception {
        given(accessChecker.canExecute(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser("folder", Folder.getRoot(), actor))
                .willReturn(folder);
        given(accessChecker.canChangeGroup(folder, actor))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(groupRepository.findByName("group2")).willReturn(Group.builder("group2").build());

        mvc.perform(patch("/files/group").with(csrf())
                        .param("path", "/folder")
                        .param("name", "group2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.group").value("group2"));
    }

    @WithMockUser
    @Test
    void shouldChangeRightWhenFileServiceCanChangeMode() throws Exception {
        given(accessChecker.canExecute(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser("folder", Folder.getRoot(), actor))
                .willReturn(folder);
        given(accessChecker.canChangeMode(folder, actor))
                .willReturn(true);
        given(fileRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(patch("/files/mode").with(csrf())
                        .param("path", "/folder")
                        .param("right", "-wx"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.access").value("dr--r-----"));
    }

    @WithMockUser
    @Test
    void shouldDownloadContentWhenFileServiceCanReadFile() throws Exception {
        given(accessChecker.canExecute(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser("file", Folder.getRoot(), actor))
                .willReturn(file);
        given(accessChecker.canRead(file, actor))
                .willReturn(true);
        given(contentRepository.readContent(file))
                .willReturn(new ByteArrayInputStream("content".getBytes()));

        mvc.perform(get("/files/download").with(csrf())
                        .param("path", "/file"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(content().bytes("content".getBytes()));
    }

    @WithMockUser
    @Test
    void shouldUploadContentWhenFileServiceCanWriteFile() throws Exception {
        given(accessChecker.canExecute(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser("file", Folder.getRoot(), actor))
                .willReturn(file);
        given(accessChecker.canWrite(file, actor))
                .willReturn(true);
        given(contentRepository.readContent(file))
                .willReturn(new ByteArrayInputStream("content".getBytes()));
        given(fileRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(multipart("/files/upload")
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

        mvc.perform(get("/files")
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(post("/files/folder").with(csrf())
                        .param("path", "/path")
                        .param("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(post("/files/file").with(csrf())
                        .param("path", "/path")
                        .param("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(patch("/files/owner").with(csrf())
                        .param("path", "/path")
                        .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(patch("/files/group").with(csrf())
                        .param("path", "/path")
                        .param("name", "group2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(patch("/files/mode").with(csrf())
                        .param("path", "/path")
                        .param("right", "-wx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(get("/files/download").with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));

        mvc.perform(multipart("/files/upload")
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
        given(accessChecker.canExecute(Folder.getRoot(), actor))
                .willReturn(true);
        given(fileRepository.findByNameAndParentAndUser("path", Folder.getRoot(), actor))
                .willReturn(null);

        mvc.perform(get("/files")
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(post("/files/folder").with(csrf())
                        .param("path", "/path")
                        .param("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(post("/files/file").with(csrf())
                        .param("path", "/path")
                        .param("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(patch("/files/owner").with(csrf())
                .param("path", "/path")
                .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(patch("/files/group").with(csrf())
                .param("path", "/path")
                .param("name", "group2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(patch("/files/mode").with(csrf())
                .param("path", "/path")
                .param("right", "-wx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(get("/files/download").with(csrf())
                .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value(startsWith("path not found in ' null:null ------rwx null' for user")));

        mvc.perform(multipart("/files/upload")
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
        given(accessChecker.canExecute(Folder.getRoot(), actor))
                .willReturn(true);
        Folder path = Folder.builder().withName("path").withParent(Folder.getRoot()).build();
        given(fileRepository.findByNameAndParentAndUser("path", Folder.getRoot(), actor))
                .willReturn(path);
        given(userRepository.findByName("user2")).willThrow(new NotFoundException("user2"));
        given(groupRepository.findByName("group2")).willThrow(new NotFoundException("group2"));

        mvc.perform(patch("/files/owner").with(csrf())
                        .param("path", "/path")
                        .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user2 not found"));

        mvc.perform(patch("/files/group").with(csrf())
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
        given(accessChecker.canExecute(Folder.getRoot(), actor))
                .willReturn(true);
        Folder path = Folder.builder().withName("path").withParent(Folder.getRoot()).build();
        given(fileRepository.findByNameAndParentAndUser("path", Folder.getRoot(), actor))
                .willReturn(path);
        given(accessChecker.canWrite(folder, actor))
                .willReturn(false);
        given(accessChecker.canRead(folder, actor))
                .willReturn(false);
        given(userRepository.findByName("user2")).willReturn(User.builder("user2").build());
        given(groupRepository.findByName("group2")).willReturn(Group.builder("group2").build());

        mvc.perform(get("/files")
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("LIST with arguments Input{item='path null:null --------- ', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(post("/files/folder").with(csrf())
                        .param("path", "/path")
                        .param("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("MKDIR with arguments Input{item='path null:null --------- ', name='folder', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(post("/files/file").with(csrf())
                        .param("path", "/path")
                        .param("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("TOUCH with arguments Input{item='path null:null --------- ', name='file', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(patch("/files/owner").with(csrf())
                        .param("path", "/path")
                        .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("CHOWN with arguments Input{item='path null:null --------- ', name='null', user=user2, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(patch("/files/group").with(csrf())
                        .param("path", "/path")
                        .param("name", "group2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("CHGRP with arguments Input{item='path null:null --------- ', name='null', user=null, group=group2, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(patch("/files/mode").with(csrf())
                        .param("path", "/path")
                        .param("right", "-wx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("CHMOD with arguments Input{item='path null:null --------- ', name='null', user=null, group=null, ownerAccess=---, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(get("/files/download").with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("DOWNLOAD with arguments Input{item='path null:null --------- ', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=null} failed for user")));

        mvc.perform(multipart("/files/upload")
                        .file("file", "content".getBytes())
                        .with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value(startsWith("UPLOAD with arguments Input{item='path null:null --------- ', name='null', user=null, group=null, ownerAccess=null, groupAccess=null, otherAccess=null, contentType=application/octet-stream} failed for user")));
    }
}
