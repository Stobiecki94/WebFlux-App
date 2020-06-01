package stobiecki.webflux.demoapp.functional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class BookConfiguration {

    @Bean
    public RouterFunction<ServerResponse> booksRoute(BookHandler bookHandler) {
        return RouterFunctions.route(GET("/books/functional"), bookHandler::getBooks);
    }
}