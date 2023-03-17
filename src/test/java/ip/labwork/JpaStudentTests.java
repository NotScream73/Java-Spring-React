package ip.labwork;

import ip.labwork.shop.model.Component;
import ip.labwork.shop.model.Order;
import ip.labwork.shop.model.Product;
import ip.labwork.shop.service.ComponentService;
import ip.labwork.shop.service.OrderService;
import ip.labwork.shop.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class JpaStudentTests {
    private static final Logger log = LoggerFactory.getLogger(JpaStudentTests.class);
    @Autowired
    ComponentService componentService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    @Test
    void test() {
        componentService.deleteAllComponent();
        productService.deleteAllProduct();
        orderService.deleteAllOrder();
        //TestCreate
        final Component component = componentService.addComponent("Огурец", 4);
        log.info(component.toString());
        Assertions.assertNotNull(component.getId());

        List<Component> componentList = new ArrayList<>();
        componentList.add(componentService.findComponent(component.getId()));
        final Product product = productService.addProduct("Бургер", 100, new Integer[]{ 2 }, componentList);
        log.info(product.toString());
        Assertions.assertNotNull(product.getId());

        List<Product> productList = new ArrayList<>();
        productList.add(productService.findProduct(product.getId()));
        final Order order = orderService.addOrder(new Date().toString(), 200, new Integer[]{ 3 }, productList);
        log.info(order.toString());
        Assertions.assertNotNull(order.getId());

        //TestRead
        final Component findComponent = componentService.findComponent(component.getId());
        log.info(findComponent.toString());
        Assertions.assertEquals(component, findComponent);

        final Product findProduct = productService.findProduct(product.getId());
        log.info(findProduct.toString());
        Assertions.assertEquals(product, findProduct);

        final Order findOrder = orderService.findOrder(order.getId());
        log.info(findOrder.toString());
        Assertions.assertEquals(order, findOrder);

        //TestReadAll
        final List<Component> components = componentService.findAllComponent();
        log.info(components.toString());
        Assertions.assertEquals(components.size(), 1);

        final List<Product> products = productService.findAllProduct();
        log.info(products.toString());
        Assertions.assertEquals(products.size(), 1);

        final List<Order> orders = orderService.findAllOrder();
        log.info(orders.toString());
        Assertions.assertEquals(orders.size(), 1);


        //TestReadNotFound
        componentService.deleteAllComponent();
        productService.deleteAllProduct();
        orderService.deleteAllOrder();

        Assertions.assertThrows(EntityNotFoundException.class, () -> componentService.findComponent(-1L));
        Assertions.assertThrows(EntityNotFoundException.class, () -> productService.findProduct(-1L));
        Assertions.assertThrows(EntityNotFoundException.class, () -> orderService.findOrder(-1L));

        //TestReadAllEmpty
        final List<Component> newComponents = componentService.findAllComponent();
        log.info(newComponents.toString());
        Assertions.assertEquals(newComponents.size(), 0);

        final List<Product> newProducts = productService.findAllProduct();
        log.info(newProducts.toString());
        Assertions.assertEquals(newProducts.size(), 0);

        final List<Order> newOrders = orderService.findAllOrder();
        log.info(newOrders.toString());
        Assertions.assertEquals(newOrders.size(), 0);
    }
}