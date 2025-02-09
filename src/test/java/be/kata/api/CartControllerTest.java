package be.kata.api;

import be.kata.api.model.CartItem;
import be.kata.api.model.Cart;
import be.kata.config.BookStoreAppConfig;
import be.kata.service.CartService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private SecurityFilterChain securityFilterChain;

    private final ObjectMapper objectMapper = new BookStoreAppConfig().objectMapper();

    @Test
    void givenCart_whenSubmit_thenReturnWithStatusCode201() throws Exception {
        Cart cart = new Cart(1, List.of(new CartItem("B1", 2), new CartItem("M1", 1)));
        mvc.perform(MockMvcRequestBuilders
                        .post("/cart")
                        .content(objectMapper.writeValueAsString(cart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void givenInvalidCart_whenSubmit_thenReturnWithStatusCode400() throws Exception {
        Cart cart = new Cart(1, null);
        mvc.perform(MockMvcRequestBuilders
                        .post("/cart")
                        .content(objectMapper.writeValueAsString(cart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidCartItem_whenSubmit_thenReturnWithStatusCode400() throws Exception {
        Cart cart = new Cart(1, List.of(new CartItem(null, 2)));
        mvc.perform(MockMvcRequestBuilders
                        .post("/cart")
                        .content(objectMapper.writeValueAsString(cart))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
