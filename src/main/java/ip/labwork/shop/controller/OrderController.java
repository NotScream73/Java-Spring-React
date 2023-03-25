package ip.labwork.shop.controller;

import ip.labwork.shop.service.ProductService;
import ip.labwork.shop.model.Order;
import ip.labwork.shop.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @PostMapping
    public OrderDTO createOrder(@RequestParam("date") String date,
                        @RequestParam("price") Integer price,
                        @RequestParam("count") Integer[] count,
                        @RequestParam("prod") Long[] prod){
        return new OrderDTO(orderService.addOrder(date, price, count, productService.findFiltredProducts(prod)));
    }
    @PutMapping("/{id}")
    public OrderDTO updateOrder(@PathVariable Long id,
                        @RequestParam("date") String date,
                        @RequestParam("price") Integer price,
                        @RequestParam("count") Integer[] count,
                        @RequestParam("prod") Long[] prod){
        return new OrderDTO(orderService.updateOrder(id, date, price, count, productService.findFiltredProducts(prod)));
    }
    @DeleteMapping("/{id}")
    public OrderDTO removeOrder(@PathVariable Long id){
        return new OrderDTO(orderService.deleteOrder(id));
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
        return orderService.findAllOrder().stream()
                .map(OrderDTO::new)
                .toList();
    }
}
