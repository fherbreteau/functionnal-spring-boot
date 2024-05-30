package io.github.fherbreteau.functional.controller;

import io.github.fherbreteau.functional.FunctionalApplication;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.driving.AccessParserService;
import io.github.fherbreteau.functional.driving.FileService;
import io.github.fherbreteau.functional.driving.UserService;
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

import static org.hamcrest.Matchers.*;
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
    private FileService fileService;
    @MockBean
    private AccessParserService accessParserService;
    @MockBean
    private UserService userService;

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
        given(userService.findUserByName("user")).willReturn(Output.success(actor));
    }

    @WithMockUser
    @Test
    void shouldReturnAListOfItemWhenFileServiceCanListElement() throws Exception {
        given(fileService.getPath("/", actor)).willReturn(Path.ROOT);
        given(fileService.processCommand(eq(ItemCommandType.LIST), eq(actor), argThat(argument -> Objects.equals(argument.getItem(), Folder.getRoot()))))
                .willReturn(Output.success(List.of(file, folder)));

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
                .andExpect(jsonPath("$[0].content-type").value(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(jsonPath("$[1].name").value("folder"))
                .andExpect(jsonPath("$[1].created").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[1].modified").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$[1].accessed").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        given(fileService.getPath("/folder", actor)).willReturn(Path.success(folder));
        given(fileService.processCommand(eq(ItemCommandType.LIST), eq(actor), argThat(argument -> Objects.equals(argument.getItem(), folder))))
                .willReturn(Output.success(List.of()));

        mvc.perform(get("/files")
                        .param("path", "/folder"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

    }

    @WithMockUser
    @Test
    void shouldCreateFileWhenFileServiceCanCreateFile() throws Exception {
        given(fileService.getPath("/", actor)).willReturn(Path.ROOT);
        given(fileService.processCommand(eq(ItemCommandType.TOUCH), eq(actor), argThat(argument ->
                Objects.equals(argument.getItem(), Folder.getRoot())
                        && Objects.equals(argument.getName(), "file"))))
                .willReturn(Output.success(file));

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
        given(fileService.getPath("/", actor)).willReturn(Path.ROOT);
        given(fileService.processCommand(eq(ItemCommandType.MKDIR), eq(actor), argThat(argument ->
                Objects.equals(argument.getItem(), Folder.getRoot())
                        && Objects.equals(argument.getName(), "folder"))))
                .willReturn(Output.success(folder));

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
        User user2 = User.builder("user2").build();
        given(userService.findUserByName("user2")).willReturn(Output.success(user2));
        given(fileService.getPath("/folder", actor)).willReturn(Path.success(folder));
        Folder updated = folder.copyBuilder().withOwner(user2).build();
        given(fileService.processCommand(eq(ItemCommandType.CHOWN), eq(actor), argThat(argument ->
                Objects.equals(argument.getItem(), folder) && Objects.equals(argument.getUser(), user2))))
                .willReturn(Output.success(updated));

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
        Group group2 = Group.builder("group2").build();
        given(userService.findGroupByName("group2")).willReturn(Output.success(group2));
        given(fileService.getPath("/folder", actor)).willReturn(Path.success(folder));
        Folder updated = folder.copyBuilder().withGroup(group2).build();
        given(fileService.processCommand(eq(ItemCommandType.CHGRP), eq(actor), argThat(argument ->
                Objects.equals(argument.getItem(), folder) && Objects.equals(argument.getGroup(), group2))))
                .willReturn(Output.success(updated));

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
        given(fileService.getPath("/folder", actor)).willReturn(Path.success(folder));
        ItemInput input = ItemInput.builder(folder).withOwnerAccess(AccessRight.writeOnly().addExecute()).build();
        given(accessParserService.parseAccessRights("-wx", folder)).willReturn(input);
        Folder updated = folder.copyBuilder().withOwnerAccess(AccessRight.readOnly()).build();
        given(fileService.processCommand(ItemCommandType.CHMOD, actor, input))
                .willReturn(Output.success(updated));

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
        given(fileService.getPath("/file", actor)).willReturn(Path.success(file));
        given(fileService.processCommand(eq(ItemCommandType.DOWNLOAD), eq(actor), argThat(argument ->
                Objects.equals(argument.getItem(), file) && Objects.equals(argument.getContentType(), file.getContentType()))))
                .willReturn(Output.success(new ByteArrayInputStream("content".getBytes())));

        mvc.perform(get("/files/download").with(csrf())
                        .param("path", "/file"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(content().bytes("content".getBytes()));
    }

    @WithMockUser
    @Test
    void shouldUploadContentWhenFileServiceCanWriteFile() throws Exception {
        given(fileService.getPath("/file", actor)).willReturn(Path.success(file));
        given(fileService.processCommand(eq(ItemCommandType.UPLOAD), eq(actor), argThat(argument ->
                Objects.equals(argument.getItem(), file))))
                .willReturn(Output.success(file));

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
    void shouldDeleteItemWhenFileServiceCanDeleteFile() throws Exception {
        given(fileService.getPath("/file", actor)).willReturn(Path.success(file));
        given(fileService.processCommand(eq(ItemCommandType.DELETE), eq(actor), argThat(argument ->
                Objects.equals(argument.getItem(), file))))
                .willReturn(Output.success(file));
        mvc.perform(delete("/files")
                        .with(csrf())
                        .param("path", "/file"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("file"));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenUserDoesNotExists() throws Exception {
        given(userService.findUserByName("user")).willReturn(Output.error("user not found"));

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
        mvc.perform(delete("/files")
                .with(csrf())
                .param("path", "/file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("UserException"))
                .andExpect(jsonPath("$.message").value("user not found"));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenPathDoesNotExists() throws Exception {
        given(fileService.getPath("/path", actor)).willReturn(Path.error(Error.error("path not found")));

        mvc.perform(get("/files")
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value("path not found"));

        mvc.perform(post("/files/folder").with(csrf())
                        .param("path", "/path")
                        .param("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value("path not found"));

        mvc.perform(post("/files/file").with(csrf())
                        .param("path", "/path")
                        .param("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value("path not found"));

        mvc.perform(patch("/files/owner").with(csrf())
                .param("path", "/path")
                .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value("path not found"));

        mvc.perform(patch("/files/group").with(csrf())
                .param("path", "/path")
                .param("name", "group2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value("path not found"));

        mvc.perform(patch("/files/mode").with(csrf())
                .param("path", "/path")
                .param("right", "-wx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value("path not found"));

        mvc.perform(get("/files/download").with(csrf())
                .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value("path not found"));

        mvc.perform(multipart("/files/upload")
                .file("file", "content".getBytes())
                .with(csrf())
                .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value("path not found"));
        mvc.perform(delete("/files")
                        .with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("PathException"))
                .andExpect(jsonPath("$.message").value("path not found"));
    }

    @WithMockUser
    @Test
    void shouldReturnAnErrorWhenParamDoesNotExists() throws Exception {
        Folder path = Folder.builder().withName("path").withParent(Folder.getRoot()).build();
        given(fileService.getPath("/path", actor)).willReturn(Path.success(path));
        given(userService.findUserByName("user2")).willReturn(Output.error("user2 not found"));
        given(userService.findGroupByName("group2")).willReturn(Output.error("group2 not found"));

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
        Folder path = Folder.builder().withName("path").withParent(Folder.getRoot()).build();
        given(fileService.getPath("/path", actor)).willReturn(Path.success(path));
        ItemInput input = ItemInput.builder(path).withOwnerAccess(AccessRight.writeOnly().addExecute()).build();
        given(accessParserService.parseAccessRights("-wx", path)).willReturn(input);
        User user2 = User.builder("user2").build();
        given(userService.findUserByName("user2")).willReturn(Output.success(user2));
        Group group2 = Group.builder("group2").build();
        given(userService.findGroupByName("group2")).willReturn(Output.success(group2));
        given(fileService.processCommand(any(), eq(actor), any())).willAnswer(invocation ->
                Output.error("Command Failed", List.of("Error")));

        mvc.perform(get("/files")
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasSize(1)))
                .andExpect(jsonPath("$.reasons[0]").value("Error"));

        mvc.perform(post("/files/folder").with(csrf())
                        .param("path", "/path")
                        .param("name", "folder"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasSize(1)))
                .andExpect(jsonPath("$.reasons[0]").value("Error"));

        mvc.perform(post("/files/file").with(csrf())
                        .param("path", "/path")
                        .param("name", "file"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasSize(1)))
                .andExpect(jsonPath("$.reasons[0]").value("Error"));

        mvc.perform(patch("/files/owner").with(csrf())
                        .param("path", "/path")
                        .param("name", "user2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasSize(1)))
                .andExpect(jsonPath("$.reasons[0]").value("Error"));

        mvc.perform(patch("/files/group").with(csrf())
                        .param("path", "/path")
                        .param("name", "group2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasSize(1)))
                .andExpect(jsonPath("$.reasons[0]").value("Error"));

        mvc.perform(patch("/files/mode").with(csrf())
                        .param("path", "/path")
                        .param("right", "-wx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasSize(1)))
                .andExpect(jsonPath("$.reasons[0]").value("Error"));

        mvc.perform(get("/files/download").with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasSize(1)))
                .andExpect(jsonPath("$.reasons[0]").value("Error"));

        mvc.perform(multipart("/files/upload")
                        .file("file", "content".getBytes())
                        .with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasSize(1)))
                .andExpect(jsonPath("$.reasons[0]").value("Error"));
        mvc.perform(delete("/files")
                        .with(csrf())
                        .param("path", "/path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type").value("CommandException"))
                .andExpect(jsonPath("$.message").value("Command Failed"))
                .andExpect(jsonPath("$.reasons").isArray())
                .andExpect(jsonPath("$.reasons", hasSize(1)))
                .andExpect(jsonPath("$.reasons[0]").value("Error"));
    }
}
