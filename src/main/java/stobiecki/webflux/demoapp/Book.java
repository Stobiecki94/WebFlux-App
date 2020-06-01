package stobiecki.webflux.demoapp;

import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Document
public class Book {

    String id;
    String title;
}