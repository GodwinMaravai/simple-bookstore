package be.kata.service;

import be.kata.api.model.CartItem;
import be.kata.persistence.book.BookEntity;
import be.kata.persistence.book.BookRepository;
import be.kata.persistence.cart.CartEntity;
import be.kata.persistence.cart.CartItemEntity;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    CartService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public boolean submit(long userId, List<CartItem> books) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Set<CartItemEntity> cartItemEntities = books.stream()
                .map(this::getCartItemEntity)
                .collect(Collectors.toSet());
        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(cartItemEntities);
        userEntity.setCart(cartEntity);
        userRepository.save(userEntity);
        return true;
    }

    private CartItemEntity getCartItemEntity(CartItem bookCount) {
        if (bookCount.count() > 0) {
            return bookRepository.findById(bookCount.bookId())
                    .map(bookEntity -> createCartItemEntity(bookCount, bookEntity))
                    .orElseThrow(() -> new IllegalArgumentException("Book id not found"));
        }
        throw new IllegalArgumentException("Book count is invalid");
    }

    private static CartItemEntity createCartItemEntity(CartItem bookCount, BookEntity bookEntity) {
        if (bookEntity.getCount() > 0 && bookEntity.getCount() >= bookCount.count()) {
            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setBookId(bookEntity.getId());
            cartItemEntity.setCount(bookCount.count());
            return cartItemEntity;
        }
        throw new IllegalArgumentException("Out of stock");
    }

}
