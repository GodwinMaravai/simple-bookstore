package be.kata.service;

import be.kata.api.model.Book;
import be.kata.api.model.Order;
import be.kata.persistence.book.BookEntity;
import be.kata.persistence.book.BookRepository;
import be.kata.persistence.cart.CartEntity;
import be.kata.persistence.cart.CartItemEntity;
import be.kata.persistence.order.OrderRepository;
import be.kata.persistence.order.OrderStatus;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final OrderService orderService = new OrderService(orderRepository, userRepository, bookRepository);

    @Test
    void givenUser_whenSubmit_thenReturnOrder() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("User2");
        userEntity.setNrn("12345678902");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("B1");
        bookEntity.setName("Book1");
        bookEntity.setAuthor("Author1");
        bookEntity.setCount(2);
        bookEntity.setPrice(50);

        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setId("B2");
        bookEntity2.setName("Book2");
        bookEntity2.setAuthor("Author2");
        bookEntity2.setCount(1);
        bookEntity2.setPrice(20);

        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setCount(2);
        cartItemEntity.setBookId("B1");
        CartItemEntity cartItemEntity2 = new CartItemEntity();
        cartItemEntity2.setCount(1);
        cartItemEntity2.setBookId("B2");
        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(Set.of(cartItemEntity, cartItemEntity2));
        userEntity.setCart(cartEntity);

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        when(bookRepository.findById("B1")).thenReturn(Optional.of(bookEntity));
        when(bookRepository.findById("B2")).thenReturn(Optional.of(bookEntity2));

        Order order = orderService.submit(1);

        assertThat(order)
                .returns(0L, Order::orderId)
                .returns(1L, Order::userId)
                .returns(OrderStatus.SUBMITTED, Order::status)
                .returns(120, Order::totalPrice)
                .returns(3, Order::totalItem);

        assertThat(order.orderedBooks().stream().sorted(Comparator.comparing(Book::id)))
                .hasSize(2)
                .satisfiesExactly(
                        book -> assertThat(book)
                                .returns(bookEntity.getId(), Book::id)
                                .returns(bookEntity.getName(), Book::title)
                                .returns(bookEntity.getAuthor(), Book::author)
                                .returns(cartItemEntity.getCount(), Book::count)
                                .returns(cartItemEntity.getCount() * bookEntity.getPrice(), Book::totalPrice),
                        book -> assertThat(book)
                                .returns(bookEntity2.getId(), Book::id)
                                .returns(bookEntity2.getName(), Book::title)
                                .returns(bookEntity2.getAuthor(), Book::author)
                                .returns(cartItemEntity2.getCount(), Book::count)
                                .returns(cartItemEntity2.getCount() * bookEntity2.getPrice(), Book::totalPrice)
                );

        verify(orderRepository).save(any());
    }
}
