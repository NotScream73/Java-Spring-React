package ip.labwork;

import ip.labwork.student.model.Component;
import ip.labwork.student.model.Ord;
import ip.labwork.student.model.Product;
import ip.labwork.student.service.ComponentService;
import ip.labwork.student.service.OrdService;
import ip.labwork.student.service.ProductService;
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
    private ComponentService componentService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrdService ordService;

    @Test
    void testComponentCreate() {
        componentService.deleteAllComponent();
        final Component component = componentService.addComponent("Помидор", 3);
        log.info(component.toString());
        Assertions.assertNotNull(component.getId());
    }

    @Test
    void testComponentRead() {
        componentService.deleteAllComponent();
        final Component component = componentService.addComponent("Помидор", 3);
        log.info(component.toString());
        final Component findComponent = componentService.findComponent(component.getId());
        log.info(findComponent.toString());
        Assertions.assertEquals(component, findComponent);
    }

    @Test
    void testComponentReadNotFound() {
        componentService.deleteAllComponent();
        Assertions.assertThrows(EntityNotFoundException.class, () -> componentService.findComponent(-1L));
    }

    @Test
    void testComponentReadAll() {
        componentService.deleteAllComponent();
        componentService.addComponent("Помидор", 3);
        componentService.addComponent("Огруец", 2);
        final List<Component> components = componentService.findAllComponent();
        log.info(components.toString());
        Assertions.assertEquals(components.size(), 2);
    }

    @Test
    void testComponentReadAllEmpty() {
        componentService.deleteAllComponent();
        final List<Component> components = componentService.findAllComponent();
        log.info(components.toString());
        Assertions.assertEquals(components.size(), 0);
    }
    @Test
    void testProductCreate() {
        productService.deleteAllProduct();
        componentService.deleteAllComponent();
        final Component component1 = componentService.addComponent("Помидор", 3);
        final Component component2 = componentService.addComponent("Булочка", 2);
        final Component component3 = componentService.addComponent("Огурец", 3);
        ArrayList<Component> components = new ArrayList<>();

        components.add(component1);
        components.add(component2);
        components.add(component3);
        final Product product = productService.addProduct("Бургер", 300, components);
        log.info(product.toString());
        final Product product1 = productService.findProduct(product.getId());
        Assertions.assertEquals(true,product1.getComponents().contains(component1));
        Assertions.assertEquals(product,product1);


        Assertions.assertEquals(product1.getComponents().size(), 3);


        productService.deleteAllProduct();
        Assertions.assertThrows(EntityNotFoundException.class, () -> productService.findProduct(-1L));
    }

    /*@Test
    void testProductRead() {
        productService.deleteAllProduct();
        final Product product = productService.addProduct("Бургер", 300);
        log.info(product.toString());
        final Product findProduct = productService.findProduct(product.getId());
        log.info(findProduct.toString());
        Assertions.assertEquals(product, findProduct);
    }*/

    @Test
    void testProductReadNotFound() {
        productService.deleteAllProduct();
        Assertions.assertThrows(EntityNotFoundException.class, () -> productService.findProduct(-1L));
    }

    @Test
    void testProductReadAll() {
        productService.deleteAllProduct();
        //productService.addProduct("Бургер", 300);
        //productService.addProduct("Хот-дог", 200);
        final List<Product> products = productService.findAllProduct();
        log.info(products.toString());
        Assertions.assertEquals(products.size(), 0);
    }

    @Test
    void testProductReadAllEmpty() {
        productService.deleteAllProduct();
        final List<Product> products = productService.findAllProduct();
        log.info(products.toString());
        Assertions.assertEquals(products.size(), 0);
    }
    /*
    @Test
    void testOrderCreate() {
        ordService.deleteAllOrd();
        final Ord ord = ordService.addOrd(new Date(), 3);
        log.info(ord.toString());
        Assertions.assertNotNull(ord.getId());
    }

    @Test
    void testOrderRead() {
        ordService.deleteAllOrd();
        final Ord Order = ordService.addOrd(new Date(), 3);
        log.info(Order.toString());
        final Ord findOrder = ordService.findOrd(Order.getId());
        log.info(findOrder.toString());
        Assertions.assertEquals(Order, findOrder);
    }

    @Test
    void testOrderReadNotFound() {
        ordService.deleteAllOrd();
        Assertions.assertThrows(EntityNotFoundException.class, () -> ordService.findOrd(-1L));
    }

    @Test
    void testOrderReadAll() {
        ordService.deleteAllOrd();
        ordService.addOrd(new Date(), 3);
        ordService.addOrd(new Date(), 2);
        final List<Ord> Orders = ordService.findAllOrd();
        log.info(Orders.toString());
        Assertions.assertEquals(Orders.size(), 2);
    }

    @Test
    void testOrderReadAllEmpty() {
        ordService.deleteAllOrd();
        final List<Ord> Orders = ordService.findAllOrd();
        log.info(Orders.toString());
        Assertions.assertEquals(Orders.size(), 0);
    }*/
}
