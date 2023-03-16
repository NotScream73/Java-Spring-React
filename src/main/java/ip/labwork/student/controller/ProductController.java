package ip.labwork.student.controller;

import ip.labwork.student.model.Component;
import ip.labwork.student.model.Product;
import ip.labwork.student.model.ProductComponents;
import ip.labwork.student.service.ComponentService;
import ip.labwork.student.service.ProductComponentsService;
import ip.labwork.student.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ComponentService componentService;
    private final ProductComponentsService productComponentsService;

    public ProductController(ProductService productService, ComponentService componentService, ProductComponentsService productComponentsService) {
        this.productService = productService;
        this.componentService = componentService;
        this.productComponentsService = productComponentsService;
    }

    @GetMapping("/add")
    public Product create(@RequestParam("name") String name,
                          @RequestParam("price") Integer price,
                          @RequestParam("count") Integer[] count,
                          @RequestParam("comp") Long[] comp){
        Product product = productService.addProduct(name,price);
        for (int i=0; i < comp.length; i++)
            productComponentsService.addProductComponents(product,componentService.findComponent(comp[i]), count[i]);
        return product;
    }
    @GetMapping("/update")
    public Product update(@RequestParam("id") Long id,
                          @RequestParam("name") String name,
                          @RequestParam("price") Integer price,
                          @RequestParam("count") Integer[] count,
                          @RequestParam("comp") Long[] comp){
        productService.updateProduct(id, name, price);

        Product product = productService.findProduct(id);
        for(int i = 0; i < comp.length; i++){
            productComponentsService.update(product, componentService.findComponent(comp[i]),count[i], comp);
        }
        return product;
    }
    @GetMapping("/remove")
    public Product remove(@RequestParam("id") Long id){
        Product product = productService.findProduct(id);
        productComponentsService.deleteProduct(product);
        return productService.deleteProduct(id);
    }
    @GetMapping("/removeAll")
    public void remove(){
        productComponentsService.deleteAllProduct();
        productService.deleteAllProduct();
    }
    @GetMapping("/find")
    public Product find(@RequestParam("id") Long id){
        return productService.findProduct(id);
    }
    @GetMapping("/findAll")
    public List<Product> findAll(){
        return productService.findAllProduct();
    }
}
