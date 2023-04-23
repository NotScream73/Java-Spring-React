package ip.labwork.shop.controller;

import ip.labwork.WebConfiguration;
import ip.labwork.shop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(WebConfiguration.REST_API + "/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderDTO createOrder(@RequestBody @Valid OrderDTO orderDTO){
        return orderService.create(orderDTO);
    }
    @PutMapping("/{id}")
    public OrderDTO updateOrder(@PathVariable Long id, @RequestBody @Valid OrderDTO orderDTO){
        return orderService.update(id, orderDTO);
    }
    @DeleteMapping("/{id}")
    public OrderDTO removeOrder(@PathVariable Long id){
        return orderService.deleteOrder(id);
    }
    @DeleteMapping
    public void removeAllOrder(){
        orderService.deleteAllOrder();
    }
    @GetMapping("/{id}")
    public OrderDTO findOrder(@PathVariable Long id){
        return new OrderDTO(orderService.findOrder(id));
    }
    @GetMapping
    public List<OrderDTO> findAllOrder(){
        return orderService.findAllOrder();
    }
}
