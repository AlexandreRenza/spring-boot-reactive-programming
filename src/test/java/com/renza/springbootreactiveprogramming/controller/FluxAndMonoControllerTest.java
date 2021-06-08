package com.renza.springbootreactiveprogramming.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest // this annotation just scan controller annotation
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void returnFlux() {

      Flux<Integer> integerflux =   webTestClient.get().uri("/flux")
                                    //.accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
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
    void returnFluxCount() {

         webTestClient.get().uri("/flux")
                //.accept(MediaType.APPLICATION_PROBLEM_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    void returnFlux2() {

       List<Integer> expectedList = Arrays.asList(1,2,3,4);

       // convert flux to a list
       EntityExchangeResult<List<Integer>> entityExchangeResult =
                 webTestClient.get().uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

       assertEquals(expectedList, entityExchangeResult.getResponseBody());
    }


    @Test
    void returnFluxWithConsumer() {

        List<Integer> expectedList = Arrays.asList(1,2,3,4);


        webTestClient.get().uri("/flux")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(Integer.class)
                        .consumeWith((response)->{
                            assertEquals(expectedList,response.getResponseBody());
                        });


    }



    @Test
    void returnFluxStream() {

        Flux<Integer> integerfluxstream =   webTestClient.get().uri("/fluxstream")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerfluxstream)
                .expectSubscription()
                .expectNext(1,2,3,4)
                .verifyComplete();

    }

    @Test
    void returnMono() {

        Integer expectedValue = new Integer(1);

        webTestClient.get().uri("/mono")
                .exchange() // connect to end point
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response -> {
                   assertEquals(expectedValue, response.getResponseBody());
                });

    }
}