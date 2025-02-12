package be.kata.api;

import be.kata.api.model.Book;
import be.kata.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> get() {
        List<Book> books = bookService.getAllBooks();
        if (isEmpty(books)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable @NotBlank String id) {
        if (!hasText(id)) {
            return ResponseEntity.badRequest().build();
        }
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> submit(@Valid @RequestBody @NotNull List<@Valid @NotNull Book> books) {
        if (isEmpty(books)) {
            return ResponseEntity.badRequest().build();
        }
        bookService.submit(books);
        return ResponseEntity.status(HttpStatus.CREATED).body("Books loaded successfully");
    }
}
