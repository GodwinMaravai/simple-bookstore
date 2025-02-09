package be.kata.persistence.order;

import be.kata.persistence.cart.CartEntity;
import be.kata.persistence.cart.CartItemEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class OrderRepositoryTest {

    private OrderEntity orderEntity;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        orderEntity = new OrderEntity();
        orderEntity.setUserId(2);
        orderEntity.setStatus(OrderStatus.SUBMITTED);

        CartItemEntity cartItemEntity1 = new CartItemEntity();
        cartItemEntity1.setBookId("B1");
        cartItemEntity1.setCount(1);

        CartItemEntity cartItemEntity2 = new CartItemEntity();
        cartItemEntity2.setBookId("B2");
        cartItemEntity2.setCount(2);

        CartEntity cartEntity = new CartEntity();
        cartEntity.setItems(Set.of(cartItemEntity1, cartItemEntity2));
        orderEntity.setCart(cartEntity);
        orderEntity.setTotalItem(10);
        orderEntity.setTotalPrice(100);

        orderRepository.save(orderEntity);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        orderRepository.delete(orderEntity);
    }

    @Test
    void givenOrderRepository_whenFindById_thenReturnOrderEntity() {
        assertThat(orderRepository.findById(1L))
                .isNotEmpty()
                .contains(orderEntity);
    }

    @Test
    void givenOrderRepository_whenSave_thenReturnOrderEntity() {
        orderEntity = new OrderEntity();
        orderEntity.setUserId(2);
        orderEntity.setStatus(OrderStatus.CANCELLED);

        assertThat(orderRepository.save(orderEntity))
                .isNotNull()
                .isEqualTo(orderEntity);

        assertThat(orderRepository.findById(orderEntity.getId())).isNotNull();
    }

}
