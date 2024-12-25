package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CopyItemCommandTest {
    private CopyItemCommand command;
    @Mock
    private ItemRepository repository;
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private AccessUpdater accessUpdater;

    private Item source;
    private Item destination;

    private User actor;

    @Captor
    private ArgumentCaptor<File> fileCaptor;

    @Captor
    private ArgumentCaptor<Folder> folderCaptor;

    @Captor
    private ArgumentCaptor<InputStream> streamCaptor;

    @Nested
    class CopySourceFileToDestinationFile {

        @BeforeEach
        public void setup() {

            source = File.builder()
                    .withName("source")
                    .withOwner(User.root())
                    .withGroup(Group.root())
                    .build();
            destination = File.builder()
                    .withName("destination")
                    .withOwner(User.root())
                    .withGroup(Group.root())
                    .build();
            actor = User.builder("actor").build();
            command = new CopyItemCommand(repository, contentRepository, accessUpdater, source, destination);
        }

        @Test
        void shouldCreateDestinationFileWithSameContentAsSourceFile() {
            // GIVEN
            given(repository.create(any())).willAnswer(invocation -> invocation.getArgument(0));
            given(accessUpdater.createItem(any(File.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(contentRepository.initContent(any(File.class))).willAnswer(invocation -> Output.success(invocation.getArgument(0)));
            given(contentRepository.readContent(any())).willReturn(Output.success(new ByteArrayInputStream("content".getBytes())));
            given(contentRepository.writeContent(any(), any())).willAnswer(invocation -> Output.success(invocation.getArgument(0)));
            // WHEN
            Output<Item> result = command.execute(actor);
            //THEN
            assertThat(result).isNotNull()
                    .extracting(Output::isSuccess, BOOLEAN)
                    .isTrue();
            verify(repository).create(fileCaptor.capture());
            assertThat(fileCaptor.getValue())
                    .extracting(Item::getName)
                    .isEqualTo("destination");
            assertThat(fileCaptor.getValue())
                    .extracting(Item::getOwner)
                    .isEqualTo(actor);
            assertThat(fileCaptor.getValue())
                    .extracting(Item::getGroup)
                    .isEqualTo(actor.getGroup());

            verify(contentRepository).initContent(any());
            verify(contentRepository).readContent((File) source);
            verify(contentRepository).writeContent(any(), streamCaptor.capture());

            assertThat(streamCaptor.getValue())
                    .isInstanceOf(ByteArrayInputStream.class)
                    .hasContent("content");
        }

        @Test
        void shouldFailWhenCreateDestinationFails() {
            // GIVEN
            given(repository.create(any())).willAnswer(invocation -> invocation.getArgument(0));
            given(accessUpdater.createItem(any(File.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(contentRepository.initContent(any(File.class))).willAnswer(invocation -> Output.error("Fail to " +
                    "create file"));
            // WHEN
            Output<Item> result = command.execute(actor);
            //THEN
            assertThat(result).isNotNull()
                    .extracting(Output::isError, BOOLEAN)
                    .isTrue();
            assertThat(result)
                    .extracting(Output::getError, type(Error.class))
                    .extracting(Error::getMessage, STRING)
                    .isEqualTo("Fail to create file");
        }

        @Test
        void shouldFailWhenSourceReadFails() {
            // GIVEN
            given(repository.create(any())).willAnswer(invocation -> invocation.getArgument(0));
            given(accessUpdater.createItem(any(File.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(contentRepository.initContent(any(File.class))).willAnswer(invocation -> Output.success(invocation.getArgument(0)));
            given(contentRepository.readContent(any())).willReturn(Output.error("Could not read file"));
            // WHEN
            Output<Item> result = command.execute(actor);
            //THEN
            assertThat(result).isNotNull()
                    .extracting(Output::isError, BOOLEAN)
                    .isTrue();
            assertThat(result)
                    .extracting(Output::getError, type(Error.class))
                    .extracting(Error::getMessage, STRING)
                    .isEqualTo("Could not read file");
        }
    }

    @Nested
    class CopySourceFileToDestinationFolder {

        @BeforeEach
        public void setup() {

            source = File.builder()
                    .withName("source")
                    .withOwner(User.root())
                    .withGroup(Group.root())
                    .build();
            destination = Folder.builder()
                    .withName("destination")
                    .withOwner(User.root())
                    .withGroup(Group.root())
                    .build();
            actor = User.builder("actor").build();
            command = new CopyItemCommand(repository, contentRepository, accessUpdater, source, destination);
        }

        @Test
        void shouldCreateDestinationFileWithSameContentAsSourceFile() {
            // GIVEN
            given(repository.create(any())).willAnswer(invocation -> invocation.getArgument(0));
            given(accessUpdater.createItem(any(File.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(contentRepository.initContent(any(File.class))).willAnswer(invocation -> Output.success(invocation.getArgument(0)));
            given(contentRepository.readContent(any())).willReturn(Output.success(new ByteArrayInputStream("content".getBytes())));
            given(contentRepository.writeContent(any(), any())).willAnswer(invocation -> Output.success(invocation.getArgument(0)));
            // WHEN
            Output<Item> result = command.execute(actor);
            //THEN
            assertThat(result).isNotNull()
                    .extracting(Output::isSuccess, BOOLEAN)
                    .isTrue();
            verify(repository).create(fileCaptor.capture());
            assertThat(fileCaptor.getValue())
                    .extracting(Item::getParent)
                    .isEqualTo(destination);
            assertThat(fileCaptor.getValue())
                    .extracting(Item::getName)
                    .isEqualTo("source");
            assertThat(fileCaptor.getValue())
                    .extracting(Item::getOwner)
                    .isEqualTo(actor);
            assertThat(fileCaptor.getValue())
                    .extracting(Item::getGroup)
                    .isEqualTo(actor.getGroup());

            verify(contentRepository).initContent(any());
            verify(contentRepository).readContent((File) source);
            verify(contentRepository).writeContent(any(), streamCaptor.capture());

            assertThat(streamCaptor.getValue())
                    .isInstanceOf(ByteArrayInputStream.class)
                    .hasContent("content");
        }

        @Test
        void shouldFailWhenCreateDestinationFails() {
            // GIVEN
            given(repository.create(any())).willAnswer(invocation -> invocation.getArgument(0));
            given(accessUpdater.createItem(any(File.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(contentRepository.initContent(any(File.class))).willAnswer(invocation -> Output.error("Fail to " +
                    "create file"));
            // WHEN
            Output<Item> result = command.execute(actor);
            //THEN
            assertThat(result).isNotNull()
                    .extracting(Output::isError, BOOLEAN)
                    .isTrue();
            assertThat(result)
                    .extracting(Output::getError, type(Error.class))
                    .extracting(Error::getMessage, STRING)
                    .isEqualTo("Fail to create file");
        }

        @Test
        void shouldFailWhenSourceReadFails() {
            // GIVEN
            given(repository.create(any())).willAnswer(invocation -> invocation.getArgument(0));
            given(accessUpdater.createItem(any(File.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(contentRepository.initContent(any(File.class))).willAnswer(invocation -> Output.success(invocation.getArgument(0)));
            given(contentRepository.readContent(any())).willReturn(Output.error("Could not read file"));
            // WHEN
            Output<Item> result = command.execute(actor);
            //THEN
            assertThat(result).isNotNull()
                    .extracting(Output::isError, BOOLEAN)
                    .isTrue();
            assertThat(result)
                    .extracting(Output::getError, type(Error.class))
                    .extracting(Error::getMessage, STRING)
                    .isEqualTo("Could not read file");
        }
    }

    @Nested
    class CopySourceFolderToDestinationFolder {

        @BeforeEach
        public void setup() {

            source = Folder.builder()
                    .withName("source")
                    .withOwner(User.root())
                    .withGroup(Group.root())
                    .build();
            destination = Folder.builder()
                    .withName("destination")
                    .withOwner(User.root())
                    .withGroup(Group.root())
                    .build();
            actor = User.builder("actor").build();
            command = new CopyItemCommand(repository, contentRepository, accessUpdater, source, destination);
        }

        @Test
        void shouldCreateDestinationFileWithSameContentAsSourceFile() {
            // GIVEN
            given(repository.create(any())).willAnswer(invocation -> invocation.getArgument(0));
            given(accessUpdater.createItem(any(Folder.class))).willAnswer(invocation -> invocation.getArgument(0));
            // WHEN
            Output<Item> result = command.execute(actor);
            //THEN
            assertThat(result).isNotNull()
                    .extracting(Output::isSuccess, BOOLEAN)
                    .isTrue();
            verify(repository).create(folderCaptor.capture());
            assertThat(folderCaptor.getValue())
                    .extracting(Item::getParent)
                    .isEqualTo(destination);
            assertThat(folderCaptor.getValue())
                    .extracting(Item::getName)
                    .isEqualTo("source");
            assertThat(folderCaptor.getValue())
                    .extracting(Item::getOwner)
                    .isEqualTo(actor);
            assertThat(folderCaptor.getValue())
                    .extracting(Item::getGroup)
                    .isEqualTo(actor.getGroup());
        }
    }
}
