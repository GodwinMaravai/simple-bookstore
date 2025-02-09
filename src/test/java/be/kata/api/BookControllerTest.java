package be.kata.api;

import be.kata.api.model.Book;
import be.kata.config.BookStoreAppConfig;
import be.kata.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private SecurityFilterChain securityFilterChain;

    private final ObjectMapper objectMapper = new BookStoreAppConfig().objectMapper();

    @Test
    void giveMockMvc_whenGet_thenReturnBooksWithStatusCode200() throws Exception {
        Book book1 = new Book("B1", "Book1", "Author1", 1, 1);
        Book book2 = new Book("B2", "Book2", "Author2", 5, 2);

        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        assertThat(mvc.perform(MockMvcRequestBuilders
                        .get("/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString())
                .isEqualTo("[{\"id\":\"B1\",\"title\":\"Book1\",\"author\":\"Author1\",\"price\":1,\"count\":1}," +
                        "{\"id\":\"B2\",\"title\":\"Book2\",\"author\":\"Author2\",\"price\":5,\"count\":2}]");
        verify(bookService).getAllBooks();
    }

    @Test
    void giveMockMvcAndEmptyBooks_whenGet_thenReturnNoContentStatus() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders
                        .get("/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(bookService).getAllBooks();
    }

    @Test
    void givenId_whenGetById_thenReturnBookWithStatusCode200() throws Exception {
        Book book1 = new Book("B1", "Book1", "Author1", 2, 1);

        when(bookService.getBookById("B1")).thenReturn(Optional.of(book1));

        assertThat(mvc.perform(MockMvcRequestBuilders
                        .get("/books/{id}", "B1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString())
                .isEqualTo("{\"id\":\"B1\",\"title\":\"Book1\",\"author\":\"Author1\",\"price\":2,\"count\":1}");
        verify(bookService).getBookById("B1");
    }

    @Test
    void givenIdAsWhitespace_whenGetById_thenReturnBadRequestStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/books/{id}", " ")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookService);
    }

    @Test
    void givenIdAsEmpty_whenGetById_thenReturnNotFoundStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/books/{id}", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verifyNoInteractions(bookService);
    }


    @Test
    void givenIdThatIsNotAvailable_whenGetById_thenReturnNoContent() throws Exception {
        when(bookService.getBookById("B1")).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders
                        .get("/books/{id}", "B1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(bookService).getBookById("B1");
    }

    @Test
    void givenBooks_whenSubmit_thenReturnWithStatusCode201() throws Exception {
        Book book = new Book("B1", "Book1", "Author1", 10, 2);
        mvc.perform(MockMvcRequestBuilders
                        .post("/books")
                        .content(objectMapper.writeValueAsString(List.of(book)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
