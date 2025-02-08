package be.kata.service;

import be.kata.api.model.Book;
import be.kata.persistence.book.BookEntity;
import be.kata.persistence.book.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceTest {

    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final BookService bookService = new BookService(bookRepository);

    @Test
    void givenBookService_whenGetAllBooks_thenReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(new BookEntity(), new BookEntity()));

        assertThat(bookService.getAllBooks()).isNotNull().hasSize(2);
        verify(bookRepository).findAll();
    }

    @Test
    void givenBookId_whenGetBookById_thenReturnABook() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("B1");
        bookEntity.setName("Book1");
        bookEntity.setAuthor("Author1");
        bookEntity.setCount(2);
        when(bookRepository.findById("B1")).thenReturn(Optional.of(bookEntity));

        Book book = new Book("B1", "Book1", "Author1", 2);

        assertThat(bookService.getBookById("B1")).isNotNull().hasValue(book);
        verify(bookRepository).findById("B1");
    }

    @Test
    void givenUser_whenSubmit_thenReturnTrue() {
        Book book = new Book("B1", "Book1", "Author1", 2);

        assertThat(bookService.submit(List.of(book))).isTrue();

        ArgumentCaptor<Set<BookEntity>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        verify(bookRepository).saveAll(argumentCaptor.capture());
        BookEntity bookEntity = argumentCaptor.getValue().stream().findFirst().get();
        assertThat(bookEntity)
                .returns("B1", BookEntity::getId)
                .returns("Book1", BookEntity::getName)
                .returns("Author1", BookEntity::getAuthor)
                .returns(2, BookEntity::getCount);
    }
}
