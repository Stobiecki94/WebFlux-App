package stobiecki.webflux.demoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class BookControllerTest {

    @LocalServerPort
    int localServerPort;

    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookRepository bookRepository;

    @Before
    public void setUp() throws Exception {
        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofSeconds(20))
                .build();

        webClient = WebClient.builder()
                .baseUrl(String.format("http://localhost:%d", localServerPort))
                .codecs(configurer -> {
                    configurer.defaultCodecs()
                            .jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.APPLICATION_STREAM_JSON, MediaType.APPLICATION_OCTET_STREAM));
                    configurer.defaultCodecs()
                            .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.APPLICATION_STREAM_JSON, MediaType.APPLICATION_OCTET_STREAM));
                })
                .build();

        BDDMockito.given(bookRepository.findAll()).willReturn(Flux.just(
                new Book("1", "Harry Potter"),
                new Book("2", "Gra o tron"),
                new Book("3", "Szybcy i wściekli"),
                new Book("4", "Transformers"),
                new Book("5", "Kierunek noc")));
    }

    @Test
    public void messageWhenNotAuthenticated() {
        this.webTestClient
                .get()
                .uri("/books/annotation")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .isEqualTo(Arrays.asList(
                        new Book("1", "Harry Potter"),
                        new Book("2", "Gra o tron"),
                        new Book("3", "Szybcy i wściekli"),
                        new Book("4", "Transformers"),
                        new Book("5", "Kierunek noc")));
    }

    @Test
    public void applicationJson() {
        log.info("application/json");
        webClient
                .get()
                .uri("/books/annotation")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Book.class)
                .doOnNext(next -> log.info("next {}", next))
                .blockLast(Duration.ofSeconds(20));
    }

    @Test
    public void applicationStreamJson() {
        log.info("application/stream+json");
        webClient
                .get()
                .uri("/books/annotation")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(Book.class)
                .doOnNext(next -> log.info("next {}", next))
                .blockLast(Duration.ofSeconds(20));
    }
}
