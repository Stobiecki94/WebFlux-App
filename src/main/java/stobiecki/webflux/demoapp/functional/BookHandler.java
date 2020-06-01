package stobiecki.webflux.demoapp.functional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import stobiecki.webflux.demoapp.Book;
import stobiecki.webflux.demoapp.BookRepository;

import java.time.Duration;

@Component
@RequiredArgsConstructor
class BookHandler {

    private final BookRepository bookRepository;

    Mono<ServerResponse> getBooks(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(bookRepository.findAll()
                                .delayElements(Duration.ofSeconds(3)),
                        Book.class);
    }
}
