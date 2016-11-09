package com.gft.technicalchallenge.controller;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class})
@WebAppConfiguration
public class ObserverIT {

    @Autowired
    private
    ObserverController observerController;

    private MockMvc mockMvc;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(observerController)
                .build();
    }

    @Test
    public void shouldBeOkWhenPathExistPaths() throws Exception {
        mockMvc.perform(post("/app/start").
                contentType(MediaType.TEXT_PLAIN)
                .content(temporaryFolder.getRoot().toPath().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldResultWithFileNotFoundWhenPathNotExist() throws Exception {
        mockMvc.perform(post("/app/start")
            .contentType(MediaType.TEXT_PLAIN)
            .content("nonPath"))
            .andExpect(content().string("File not found"));
    }

    @Test
    public void shouldCloseResourcesOnAppStop() throws Exception {
        mockMvc.perform(post("/app/stop"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnSameEndPointForSamePaths() throws IOException {
        ResponseEntity<String> firstEndpoint = observerController.startObserving(temporaryFolder.getRoot().toPath().toString());
        ResponseEntity<String> secondEndpoint = observerController.startObserving(temporaryFolder.getRoot().toPath().toString());

        Assertions.assertThat(firstEndpoint.equals(secondEndpoint));
    }

    @Test
    public void shouldReturnNotDirectory() throws Exception {
        mockMvc.perform(post("/app/start")
                .contentType(MediaType.TEXT_PLAIN)
                .content(temporaryFolder.newFile().toPath().toString()))
                .andExpect(content().string("Not directory"));
    }

    @Test
    public void shouldCloseResources() throws IOException {

        observerController.startObserving(temporaryFolder.getRoot().toPath().toString());
        File file = temporaryFolder.newFolder("test");

        ResponseEntity<String> secondEndpoint = observerController.startObserving(file.toPath().toString());

        observerController.stopObserving();

        ResponseEntity<String> thirdEndPoint = observerController.startObserving(file.toPath().toString());

        Assertions.assertThat(thirdEndPoint).isNotEqualTo(secondEndpoint);

    }

}