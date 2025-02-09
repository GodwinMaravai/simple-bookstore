package be.kata.api;

import be.kata.api.model.Order;
import be.kata.api.model.UserOrderStatus;
import be.kata.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.ObjectUtils.isEmpty;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@Valid @NotNull @PathVariable long id) {
        Order order = orderService.get(id);
        if (isEmpty(order)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> submit(@RequestBody @NotBlank String name) {
        Order order = orderService.submit(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping
    public ResponseEntity<Order> update(@RequestBody @NotNull @Valid UserOrderStatus userOrderStatus) {
        Order order = orderService.updateStatus(userOrderStatus.userId(), userOrderStatus.orderId(), userOrderStatus.status());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
