package com.gft.technicalchallenge.controller;

import com.gft.technicalchallenge.factory.TreeReactiveStreamFactory;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

        ResponseEntity<String> endPoint = restTemplate.getForEntity("/app/obtainEndPoint", String.class);

        String path = temporaryFolder.getRoot().getAbsolutePath();
        HttpHeaders requestHeadersSession = new HttpHeaders();
        requestHeadersSession.set("Cookie", endPoint.getHeaders().get("Set-Cookie").get(0));
        HttpEntity<String> requestEntityToKeepSession = new HttpEntity<>(path, requestHeadersSession);


        ResponseEntity responseEntity = restTemplate.postForEntity("/app/start/" + endPoint.getBody(), requestEntityToKeepSession, String.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldResultWithFileNotFoundWhenPathNotExist() throws Exception {

        ResponseEntity<String> endPoint = restTemplate.getForEntity("/app/obtainEndPoint", String.class);

        HttpHeaders requestHeadersSession = new HttpHeaders();
        requestHeadersSession.set("Cookie", endPoint.getHeaders().get("Set-Cookie").get(0));
        HttpEntity<String> requestEntityToKeepSession = new HttpEntity<>("nonPath", requestHeadersSession);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/app/start/" + endPoint.getBody(), requestEntityToKeepSession, String.class);

        Assertions.assertThat(responseEntity.getBody()).isEqualTo("\"File not found\"");
    }

    @Test
    public void shouldResultWithNotDirectoryWhenTryToObserveFile() throws Exception {
        temporaryFolder.newFile("test");
        ResponseEntity<String> endPoint = restTemplate.getForEntity("/app/obtainEndPoint", String.class);
        HttpHeaders requestHeadersSession = new HttpHeaders();
        requestHeadersSession.set("Cookie", endPoint.getHeaders().get("Set-Cookie").get(0));
        HttpEntity<String> requestEntityToKeepSession = new HttpEntity<>(temporaryFolder.getRoot().getAbsolutePath()+"/test", requestHeadersSession);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/app/start/" + endPoint.getBody(), requestEntityToKeepSession, String.class);

        Assertions.assertThat(responseEntity.getBody()).isEqualTo("\"Not directory\"");
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
    public void shouldReturnDifferentStreamsForSamePathWhenSessionEnds() throws IOException, InterruptedException {
        String path = temporaryFolder.getRoot().getAbsolutePath();

        ResponseEntity<String> endPointFirstSession = restTemplate.getForEntity("/app/obtainEndPoint", String.class);
        ResponseEntity<String> endPointSecondSession = restTemplate.getForEntity("/app/obtainEndPoint", String.class);

        HttpHeaders requestHeadersSession = new HttpHeaders();
        requestHeadersSession.set("Cookie", endPointFirstSession.getHeaders().get("Set-Cookie").get(0));
        HttpEntity<String> requestEntityToKeepFirstSession = new HttpEntity<>(path, requestHeadersSession);

        requestHeadersSession = new HttpHeaders();
        requestHeadersSession.set("Cookie", endPointSecondSession.getHeaders().get("Set-Cookie").get(0));
        HttpEntity<String> requestEntityToKeepSecondSession = new HttpEntity<>(path, requestHeadersSession);


        restTemplate.postForEntity("/app/start/" + endPointFirstSession.getBody(), requestEntityToKeepFirstSession, String.class);
        TreeReactiveStream stream1 = factory.getReactiveStream(Paths.get(path));

        restTemplate.postForEntity("/app/endSession", requestEntityToKeepFirstSession, String.class);

        restTemplate.postForEntity("/app/start/" + endPointSecondSession.getBody(), requestEntityToKeepSecondSession, String.class);
        TreeReactiveStream stream2 = factory.getReactiveStream(Paths.get(path));

        Assertions.assertThat(stream1).isNotSameAs(stream2);
    }



}