package com.renza.springbootreactiveprogramming.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
class SampleHandlerFunctionTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux() {

        Flux<Integer> integerflux =   webTestClient.get().uri("/functional/flux")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerflux)
                .expectSubscription()
                .expectNext(1,2,3,4)
                .verifyComplete();


    }

    @Test
    void mono() {
        Integer expectedValue = new Integer(1);

        webTestClient.get().uri("/functional/mono")
                .exchange() // connect to end point
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response -> {
                    assertEquals(expectedValue, response.getResponseBody());
                });

    }
}