package com.gft.technicalchallenge.controller;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilesControllerTest {

    @Autowired
    private
    TestRestTemplate restTemplate;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldCreateFileWhenFileDontExist(){

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/addFile", temporaryFolder.getRoot().getAbsolutePath()+"/test.txt", String.class);

        Assertions.assertThat(new File(temporaryFolder.getRoot().getAbsolutePath()+"/test.txt").exists());
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("\"test.txt created\"");
    }

    @Test
    public void shouldReturnInformationWhenFileExist() throws IOException {

        temporaryFolder.newFile("test.txt");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/addFile", temporaryFolder.getRoot().getAbsolutePath()+"/test.txt", String.class);

        Assertions.assertThat(new File(temporaryFolder.getRoot().getAbsolutePath()+"/test.txt").exists());
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("\"test.txt already exist\"");

    }

    @Test
    public void shouldRemoveExistingFile() throws IOException {

        temporaryFolder.newFile("test.txt");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/removeFile", temporaryFolder.getRoot().getAbsolutePath()+"/test.txt", String.class);

        Assertions.assertThat(new File(temporaryFolder.getRoot().getAbsolutePath()+"/test.txt").exists());
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("\"test.txt deleted\"");

    }

    @Test
    public void shouldReturnInformationWhenFileNotExist(){

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/removeFile", temporaryFolder.getRoot().getAbsolutePath()+"/test.txt", String.class);

        Assertions.assertThat(new File(temporaryFolder.getRoot().getAbsolutePath()+"/test.txt").exists());
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("\"test.txt can't be deleted\"");

    }



}
