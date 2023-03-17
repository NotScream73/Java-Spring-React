package ip.labwork.shop.controller;

import ip.labwork.shop.service.ProductService;
import ip.labwork.shop.model.Order;
import ip.labwork.shop.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/add")
    public Order create(@RequestParam("date") String date,
                        @RequestParam("price") Integer price,
                        @RequestParam("count") Integer[] count,
                        @RequestParam("prod") Long[] prod){
        return orderService.addOrder(date, price, count, productService.findFiltredProducts(prod));
    }
    @GetMapping("/update")
    public Order update(@RequestParam("id") Long id,
                        @RequestParam("date") String date,
                        @RequestParam("price") Integer price,
                        @RequestParam("count") Integer[] count,
                        @RequestParam("prod") Long[] prod){
        return orderService.updateOrder(id, date, price, count, productService.findFiltredProducts(prod));
    }
    @GetMapping("/remove")
    public Order remove(@RequestParam("id") Long id){
        return orderService.deleteOrder(id);
    }
    @GetMapping("/removeAll")
    public void remove(){
        orderService.deleteAllOrder();
    }
    @GetMapping("/find")
    public Order find(@RequestParam("id") Long id){
        return orderService.findOrder(id);
    }
    @GetMapping("/findAll")
    public List<Order> findAll(){
        return orderService.findAllOrder();
    }
}
