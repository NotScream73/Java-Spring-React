package ip.labwork;

import ip.labwork.student.model.Component;
import ip.labwork.student.model.Product;
import ip.labwork.student.model.ProductComponents;
import ip.labwork.student.service.ComponentService;
import ip.labwork.student.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class JpaStudentTests {
    private static final Logger log = LoggerFactory.getLogger(JpaStudentTests.class);
    @Autowired
    ComponentService componentService;
    @Autowired
    ProductService productService;
    /*@Test
    void test(){
        Component component = componentService.addComponent("Помидор", 10);
        Component component1 = componentService.addComponent("Огурец", 20);
        Set<ProductComponents> temp = new HashSet<>();
        ProductComponents tem = new ProductComponents();
        tem.setComponent(component);
        tem.setCount(5);
        ProductComponents te = new ProductComponents();
        te.setComponent(component1);
        te.setCount(6);
        temp.add(tem);
        temp.add(te);
        Product product = new Product("Гамбургер", 100, temp);
        productService.check();
        componentService.check();
        productService.addProduct("Гамбургер", 100, temp);
        productService.check();
        componentService.check();
    }*/
}