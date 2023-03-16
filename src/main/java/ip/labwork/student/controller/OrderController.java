package ip.labwork.student.controller;

import ip.labwork.student.model.Order;
import ip.labwork.student.model.Product;
import ip.labwork.student.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderProductsService orderProductsService;

    public OrderController(OrderService orderService, ProductService productService, OrderProductsService orderProductsService) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderProductsService = orderProductsService;
    }

    @GetMapping("/add")
    public Order create(@RequestParam("date") String date,
                        @RequestParam("price") Integer price,
                        @RequestParam("count") Integer[] count,
                        @RequestParam("prod") Long[] prod){
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        Date newDate;
        try{
            newDate = format.parse(date);
        }catch (Exception exception){
            newDate = new Date();
        }
        Order order = orderService.addOrder(newDate, price);
        for (int i=0; i < prod.length; i++)
            orderProductsService.addOrderProducts(order, productService.findProduct(prod[i]), count[i]);
        return order;
    }
    @GetMapping("/update")
    public Order update(@RequestParam("id") Long id,
                        @RequestParam("date") Date date,
                        @RequestParam("price") Integer price,
                        @RequestParam("count") Integer[] count,
                        @RequestParam("prod") Long[] prod){
        orderService.updateOrder(id, date, price);
        orderProductsService.removeAll(id, prod);
        Order order = orderService.findOrder(id);
        for(int i = 0; i < prod.length; i++){
            orderProductsService.addOrderProducts(order, productService.findProduct(prod[i]),count[i]);
        }
        return order;
    }
    @GetMapping("/remove")
    public Order remove(@RequestParam("id") Long id){
        Order order = orderService.findOrder(id);
        orderProductsService.deleteOrder(order);
        return orderService.deleteOrder(id);
    }
    @GetMapping("/removeAll")
    public void remove(){
        orderProductsService.deleteAllOrder();
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
