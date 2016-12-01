package com.gft.technicalchallenge.controller;

import com.gft.technicalchallenge.factory.TreeReactiveStreamFactory;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.io.IOException;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ObserverITTest {

    @Autowired
    private
    TreeReactiveStreamFactory factory;

    @Autowired
    private TestRestTemplate restTemplate;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldBeOkWhenPathExist() throws Exception {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/app/start", temporaryFolder.getRoot().getAbsolutePath(), String.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldResultWithFileNotFoundWhenPathNotExist() throws Exception {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/app/start", "nonPath", String.class);

        Assertions.assertThat(responseEntity.getBody()).isEqualTo("\"File not found\"");
    }

    @Test
    public void shouldReturnSameStreamForSamePathForAllSession() throws IOException {
        String path = temporaryFolder.getRoot().getAbsolutePath();

        HttpHeaders requestHeadersSession1 = new HttpHeaders();
        requestHeadersSession1.add("Cookie", "JSESSIONID=" + "1");
        HttpEntity<String> requestEntitySession1 = new HttpEntity<>(path, requestHeadersSession1);

        HttpHeaders requestHeadersSession2 = new HttpHeaders();
        requestHeadersSession2.add("Cookie", "JSESSIONID=" + "2");
        HttpEntity<String> requestEntitySession2 = new HttpEntity<>(path, requestHeadersSession2);


        restTemplate.postForEntity("/app/start", requestEntitySession1, String.class);
        TreeReactiveStream stream1 = factory.getReactiveStream(Paths.get(path));
        restTemplate.postForEntity("/app/start", requestEntitySession2, String.class);
        TreeReactiveStream stream2 = factory.getReactiveStream(Paths.get(path));

        Assertions.assertThat(stream1).isSameAs(stream2);
    }

    @Test
    public void shouldReturnDifferentStreamsWhenSessionEnds() throws IOException, InterruptedException {
        String path = temporaryFolder.getRoot().getAbsolutePath();

        ResponseEntity<String> responseEntity1 = restTemplate.postForEntity("/app/start", path, String.class);
        TreeReactiveStream stream1 = factory.getReactiveStream(Paths.get(path));

        HttpHeaders requestHeadersSession = new HttpHeaders();
        requestHeadersSession.set("Cookie", responseEntity1.getHeaders().get("Set-Cookie").get(0));
        HttpEntity<String> requestEntityToKeepSession = new HttpEntity<>(path, requestHeadersSession);
        restTemplate.postForEntity("/app/endSession", requestEntityToKeepSession, String.class);

        restTemplate.postForEntity("/app/start", path, String.class);
        TreeReactiveStream stream2 = factory.getReactiveStream(Paths.get(path));

        Assertions.assertThat(stream1).isNotSameAs(stream2);
    }



}