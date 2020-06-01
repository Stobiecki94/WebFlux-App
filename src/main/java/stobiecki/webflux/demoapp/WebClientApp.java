package stobiecki.webflux.demoapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class WebClientApp {

    public static void main(String[] args) {
        WebClient.create("http://localhost:8080")
                .get()
                .uri("/books")
                .retrieve()
                .bodyToFlux(Book.class)
                .doOnNext(next -> log.info("next: {}", next))
                .blockLast();
    }
}
