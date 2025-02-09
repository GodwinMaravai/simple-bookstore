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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    public Order submit(String username) {
        UserEntity userEntity = Optional.of(userRepository.findUserEntityByName(username))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userEntity.getId());
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

    public Order updateStatus(long userId, long orderId, OrderStatus orderStatus) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (userId == orderEntity.getUserId()) {
            if (orderEntity.getStatus().equals(OrderStatus.SUBMITTED)) {
                orderEntity.setStatus(orderStatus);
                List<Book> books = Collections.emptyList();
                if (OrderStatus.CANCELLED.equals(orderStatus)) {
                    orderEntity.setCart(null);
                } else if (OrderStatus.COMPLETED.equals(orderStatus)) {
                    UserEntity userEntity = userRepository.findById(orderEntity.getUserId())
                            .orElseThrow(() -> new IllegalArgumentException("User not found"));
                    userEntity.setCart(null);
                    userRepository.save(userEntity);
                    books = getOrderedBooks(orderEntity);
                }
                orderRepository.save(orderEntity);
                return new Order(orderEntity.getId(), orderEntity.getUserId(), orderEntity.getStatus(), orderEntity.getTotalPrice(), orderEntity.getTotalItem(), books);
            }
            throw new IllegalArgumentException("The order '%s' with status '%s' should not be updated");
        }
        throw new IllegalArgumentException("Invalid request");
    }

    public Order get(long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return new Order(orderEntity.getId(), orderEntity.getUserId(), orderEntity.getStatus(), orderEntity.getTotalPrice(), orderEntity.getTotalItem(), getOrderedBooks(orderEntity));
    }

    private List<Book> getOrderedBooks(OrderEntity orderEntity) {
        return orderEntity.getCart().getItems()
                .stream()
                .map(entity -> {
                    BookEntity bookEntity = bookRepository.findById(entity.getBookId()).orElseThrow(() -> new IllegalArgumentException("Book not found"));
                    int pricePerBook = bookEntity.getPrice() * entity.getCount();
                    return new Book(bookEntity.getId(), bookEntity.getName(), bookEntity.getAuthor(), pricePerBook, entity.getCount());
                }).toList();
    }
}
