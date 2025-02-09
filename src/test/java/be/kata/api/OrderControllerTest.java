package be.kata.api;

import be.kata.api.model.Book;
import be.kata.api.model.Order;
import be.kata.api.model.UserOrderStatus;
import be.kata.config.BookStoreAppConfig;
import be.kata.persistence.order.OrderStatus;
import be.kata.service.OrderService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private SecurityFilterChain securityFilterChain;

    private final ObjectMapper objectMapper = new BookStoreAppConfig().objectMapper();

    @Test
    void givenUserName_whenSubmit_thenReturnWithStatusCode201() throws Exception {
        Book book1 = new Book("B1", "Book1", "Author1", 2, 1);
        Order order = new Order(1, 1, OrderStatus.COMPLETED, 100, 100, List.of(book1));

        when(orderService.submit("User1")).thenReturn(order);

        assertThat(mvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .content("User1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString())
                .isEqualTo("{\"orderId\":1," +
                        "\"userId\":1," +
                        "\"status\":\"COMPLETED\"," +
                        "\"totalPrice\":100," +
                        "\"totalItem\":100," +
                        "\"orderedBooks\":[{\"id\":\"B1\",\"title\":\"Book1\",\"author\":\"Author1\",\"totalPrice\":2,\"count\":1}]}");
        verify(orderService).submit("User1");
    }

    @Test
    void givenInvalidName_whenSubmit_thenReturnWithStatusCode400() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(orderService);
    }

    @Test
    void givenId_whenGet_thenReturnBookWithStatusCode200() throws Exception {
        Book book1 = new Book("B1", "Book1", "Author1", 2, 1);
        Order order = new Order(1, 1, OrderStatus.COMPLETED, 100, 100, List.of(book1));

        when(orderService.get(1)).thenReturn(order);

        assertThat(mvc.perform(MockMvcRequestBuilders
                        .get("/order/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString())
                .isEqualTo("{\"orderId\":1," +
                        "\"userId\":1," +
                        "\"status\":\"COMPLETED\"," +
                        "\"totalPrice\":100," +
                        "\"totalItem\":100," +
                        "\"orderedBooks\":[{\"id\":\"B1\",\"title\":\"Book1\",\"author\":\"Author1\",\"totalPrice\":2,\"count\":1}]}");
        verify(orderService).get(1);
    }

    @Test
    void givenIdAsWhitespace_whenGet_thenReturnBadRequestStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/order/{id}", " ")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(orderService);
    }

    @Test
    void givenIdAsEmpty_whenGet_thenReturnNotFoundStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/order/{id}", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verifyNoInteractions(orderService);
    }

    @Test
    void givenOrderStatus_whenUpdate_thenReturnWithStatusCode201() throws Exception {
        Book book1 = new Book("B1", "Book1", "Author1", 2, 1);
        Order order = new Order(1, 1, OrderStatus.COMPLETED, 100, 100, List.of(book1));

        UserOrderStatus bookOrderStatus = new UserOrderStatus(1, 1, OrderStatus.CANCELLED);

        when(orderService.updateStatus(1, 1, OrderStatus.CANCELLED)).thenReturn(order);

        assertThat(mvc.perform(MockMvcRequestBuilders
                        .put("/order")
                        .content(objectMapper.writeValueAsString(bookOrderStatus))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString())
                .isEqualTo("{\"orderId\":1," +
                        "\"userId\":1," +
                        "\"status\":\"COMPLETED\"," +
                        "\"totalPrice\":100," +
                        "\"totalItem\":100," +
                        "\"orderedBooks\":[{\"id\":\"B1\",\"title\":\"Book1\",\"author\":\"Author1\",\"totalPrice\":2,\"count\":1}]}");
        verify(orderService).updateStatus(1, 1, OrderStatus.CANCELLED);
    }

    @Test
    void givenInvalidName_whenUpdate_thenReturnWithStatusCode400() throws Exception {
        UserOrderStatus bookOrderStatus = new UserOrderStatus(1, 1, null);

        mvc.perform(MockMvcRequestBuilders
                        .put("/order")
                        .content(objectMapper.writeValueAsString(bookOrderStatus))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(orderService);
    }
}
