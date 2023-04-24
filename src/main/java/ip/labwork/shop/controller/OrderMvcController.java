package ip.labwork.shop.controller;

import ip.labwork.shop.model.OrderStatus;
import ip.labwork.shop.service.OrderService;
import ip.labwork.shop.service.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderMvcController {
    private final OrderService orderService;
    private final ProductService productService;
    public OrderMvcController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }
    @GetMapping
    public String getOrders(HttpServletRequest request,
                            Model model) {

        Cookie[] cookies = request.getCookies();
        List<ProductDTO> productDTOS = new ArrayList<>();
        int totalPrice = 0;
        if (cookies != null){
            for(Cookie cookie : cookies){
                if (StringUtils.isNumeric(cookie.getName())){
                    ProductDTO productDTO = new ProductDTO(productService.findProduct(Long.parseLong(cookie.getName())),Integer.parseInt(cookie.getValue()));
                    productDTOS.add(productDTO);
                    totalPrice += productDTO.getPrice() * productDTO.getCount();
                }
            }
        }
        model.addAttribute("productDTOS", productDTOS);
        model.addAttribute("totalPrice", totalPrice);
        return "order";
    }
    @PostMapping
    public String createOrder(HttpServletRequest request,
                              HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        OrderDTO orderDTO = new OrderDTO();
        List<ProductDTO> productDTOS = new ArrayList<>();
        int totalPrice = 0;
        for(Cookie temp : cookies){
            if (StringUtils.isNumeric(temp.getName())){
                ProductDTO productDTO = new ProductDTO(productService.findProduct(Long.parseLong(temp.getName())),Integer.parseInt(temp.getValue()));
                productDTOS.add(productDTO);
                totalPrice += productDTO.getPrice() * productDTO.getCount();
            }
        }
        orderDTO.setPrice(totalPrice);
        orderDTO.setProductDTOList(productDTOS);
        orderDTO.setStatus(OrderStatus.Готов);
        orderService.create(orderDTO);
        response.addCookie(new Cookie("delete",""));
        return "redirect:/order";
    }
    @GetMapping(value = {"/all"})
    public String getOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrder());
        return "orders";
    }

    @GetMapping(value = {"/view/{id}"})
    public String getOrder(@PathVariable(required = false) Long id,
                              Model model) {
        model.addAttribute("orderDto", new OrderDTO(orderService.findOrder(id)));
        return "order-view";
    }
}
