package be.kata.service;

import be.kata.persistence.cart.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;

    CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
}
