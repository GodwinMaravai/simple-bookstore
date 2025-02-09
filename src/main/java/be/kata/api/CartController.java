package be.kata.api;

import be.kata.api.model.Cart;
import be.kata.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<String> submit(@Valid @RequestBody Cart userCart) {
        cartService.submit(userCart.name(), userCart.bookCounts());
        return ResponseEntity.status(HttpStatus.CREATED).body("Created cart for the user '%s'".formatted(userCart.name()));
    }
}
