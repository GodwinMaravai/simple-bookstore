package be.kata.service;

import be.kata.api.model.Book;
import be.kata.api.model.Order;
import be.kata.persistence.book.BookEntity;
import be.kata.persistence.book.BookRepository;
import be.kata.persistence.cart.CartEntity;
import be.kata.persistence.order.OrderEntity;
import be.kata.persistence.order.OrderRepository;
import be.kata.persistence.order.OrderStatus;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    OrderService(OrderRepository orderRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Order submit(long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setStatus(OrderStatus.SUBMITTED);
        CartEntity cartEntity = userEntity.getCart();

        AtomicInteger itemCounter = new AtomicInteger(0);
        AtomicInteger itemPrice = new AtomicInteger(0);

        List<Book> orderedBooks = cartEntity.getItems()
                .stream()
                .map(entity -> {
                    BookEntity bookEntity = bookRepository.findById(entity.getBookId()).orElseThrow(() -> new IllegalArgumentException("Book not found"));

                    itemCounter.addAndGet(entity.getCount());
                    int pricePerBook = bookEntity.getPrice() * entity.getCount();
                    itemPrice.addAndGet(pricePerBook);

                    return new Book(bookEntity.getId(), bookEntity.getName(), bookEntity.getAuthor(), pricePerBook, entity.getCount());
                }).toList();
        orderEntity.setCart(cartEntity);
        orderEntity.setTotalItem(itemCounter.get());
        orderEntity.setTotalPrice(itemPrice.get());

        orderRepository.save(orderEntity);

        return new Order(orderEntity.getId(), orderEntity.getUserId(), orderEntity.getStatus(), orderEntity.getTotalPrice(), orderEntity.getTotalItem(), orderedBooks);
    }
}
