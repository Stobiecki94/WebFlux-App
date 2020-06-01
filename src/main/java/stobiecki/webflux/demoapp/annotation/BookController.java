package stobiecki.webflux.demoapp.annotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import stobiecki.webflux.demoapp.Book;
import stobiecki.webflux.demoapp.BookRepository;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("books/annotation")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    //ponieważ mamy zwrócony Flux oraz produces = MediaType.APPLICATION_STREAM_JSON_VALUE nasza aplikacja powinna zwracać
    // strumień elementów, gdzie każdy z osobna będzie z serializowany do jsona (zamiast z serializowanej listy obiektów w jsonie)
    @GetMapping//(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Book> getBooks() {
//        return bookRepository.findAll()
        return Flux.just(
                new Book("1", "Harry Potter"),
                new Book("2", "Gra o tron"),
                new Book("3", "Szybcy i wściekli"),
                new Book("4", "Transformers"),
                new Book("5", "Kierunek noc"))
                .doOnNext(next -> log.info("next: {}", next))
                .delayElements(Duration.ofSeconds(1));
    }

}
