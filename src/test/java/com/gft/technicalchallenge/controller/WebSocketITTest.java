package com.gft.technicalchallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.technicalchallenge.model.Event;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketITTest {

    private static final String EVENTS_GET = "/events/get/";
    private static final String FILE_NAME = "NewFile";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private static final String WEBSOCKET_URI = "ws://localhost:8080/gs-guide-websocket";
    private BlockingQueue<Event> blockingQueue;
    private List<Transport> transportList;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp(){
        blockingQueue = new LinkedBlockingDeque<>();
        transportList = new ArrayList<>();
        transportList.add(new WebSocketTransport(new StandardWebSocketClient()));
    }

    @Test
    public void shouldReceiveStompMessageOnDirectoryCreated() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        String path = temporaryFolder.getRoot().getAbsolutePath();

        ResponseEntity<String> endPointFirstSession = restTemplate.getForEntity("/app/obtainEndPoint", String.class);
        HttpHeaders requestHeadersSession = new HttpHeaders();
        requestHeadersSession.set("Cookie", endPointFirstSession.getHeaders().get("Set-Cookie").get(0));
        HttpEntity<String> requestEntityToKeepFirstSession = new HttpEntity<>(path, requestHeadersSession);

        WebSocketStompClient client = new WebSocketStompClient(new SockJsClient(transportList));
        StompSession session = client.connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {
        }).get(5, TimeUnit.SECONDS);


        restTemplate.postForEntity("/app/start/" + endPointFirstSession.getBody(), requestEntityToKeepFirstSession, String.class).getBody();
        session.subscribe(EVENTS_GET + endPointFirstSession.getBody(), new DefaultStompFrameHandler());
        temporaryFolder.newFolder(FILE_NAME);

        Thread.sleep(1000);


        Assertions.assertThat(blockingQueue.size()).isEqualTo(1);
        Assertions.assertThat(blockingQueue.peek().getFileName()).isEqualTo(FILE_NAME);
    }

    @Test
    public void shouldSendMessageToAllSubscribers() throws InterruptedException, ExecutionException, TimeoutException, IOException {

        String path = temporaryFolder.getRoot().getAbsolutePath();

        ResponseEntity<String> endPointFirstSession = restTemplate.getForEntity("/app/obtainEndPoint", String.class);

        HttpHeaders requestHeadersSession = new HttpHeaders();
        requestHeadersSession.set("Cookie", endPointFirstSession.getHeaders().get("Set-Cookie").get(0));
        HttpEntity<String> requestEntityToKeepFirstSession = new HttpEntity<>(path, requestHeadersSession);

        WebSocketStompClient clientFirst = new WebSocketStompClient(new SockJsClient(transportList));
        StompSession sessionFirst = clientFirst.connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {})
                .get(5, TimeUnit.SECONDS);


        restTemplate.postForEntity("/app/start/" + endPointFirstSession.getBody(), requestEntityToKeepFirstSession, String.class).getBody();
        DefaultStompFrameHandler frameHandler = new DefaultStompFrameHandler();
        sessionFirst.subscribe(EVENTS_GET + endPointFirstSession.getBody(), frameHandler);
        sessionFirst.subscribe(EVENTS_GET + endPointFirstSession.getBody(), frameHandler);
        Thread.sleep(2000);
        temporaryFolder.newFile(FILE_NAME);
        Thread.sleep(2000);


        Assertions.assertThat(blockingQueue.size()).isEqualTo(2);
    }

    @Test
    public void shouldUnsubscribeFromOneEndPoint() throws InterruptedException, ExecutionException, TimeoutException, IOException {

        String path = temporaryFolder.getRoot().getAbsolutePath();

        ResponseEntity<String> endPointFirst = restTemplate.getForEntity("/app/obtainEndPoint", String.class);

        HttpHeaders requestHeadersSession = new HttpHeaders();
        requestHeadersSession.set("Cookie", endPointFirst.getHeaders().get("Set-Cookie").get(0));
        HttpEntity<String> requestEntityToKeepSession = new HttpEntity<>(path, requestHeadersSession);

        ResponseEntity<String> endPointSecond = restTemplate.exchange("/app/obtainEndPoint", HttpMethod.GET, requestEntityToKeepSession, String.class);

        WebSocketStompClient clientFirst = new WebSocketStompClient(new SockJsClient(transportList));
        StompSession sessionFirst = clientFirst.connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {})
                .get(5, TimeUnit.SECONDS);

        restTemplate.postForEntity("/app/start/" + endPointFirst.getBody(), requestEntityToKeepSession, String.class);
        restTemplate.postForEntity("/app/start/" + endPointSecond.getBody(), requestEntityToKeepSession, String.class);
        DefaultStompFrameHandler frameHandler = new DefaultStompFrameHandler();
        sessionFirst.subscribe(EVENTS_GET + endPointFirst.getBody(), frameHandler);
        sessionFirst.subscribe(EVENTS_GET + endPointSecond.getBody(), frameHandler);
        Thread.sleep(2000);
        temporaryFolder.newFile(FILE_NAME);
        Thread.sleep(2000);



        Assertions.assertThat(blockingQueue.size()).isEqualTo(2);
        restTemplate.postForEntity("/app/stop/" + endPointSecond.getBody(), requestEntityToKeepSession, null);
        temporaryFolder.newFile(FILE_NAME+"2");
        Thread.sleep(2000);
        Assertions.assertThat(blockingQueue.size()).isEqualTo(3);

    }

    class DefaultStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            System.out.println("Header is " + stompHeaders.toString());
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            ObjectMapper mapper = new ObjectMapper();
            Event event = new Event();
            try {
                event = mapper.readValue(new String((byte[]) o), Event.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            blockingQueue.offer(event);
        }

    }

}
