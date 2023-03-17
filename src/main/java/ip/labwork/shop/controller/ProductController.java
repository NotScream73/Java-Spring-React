package ip.labwork.shop.controller;

import ip.labwork.shop.service.ProductService;
import ip.labwork.shop.model.Product;
import ip.labwork.shop.service.ComponentService;
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

    public ProductController(ProductService productService, ComponentService componentService) {
        this.productService = productService;
        this.componentService = componentService;
    }

    @GetMapping("/add")
    public Product create(@RequestParam("name") String name,
                          @RequestParam("price") Integer price,
                          @RequestParam("count") Integer[] count,
                          @RequestParam("comp") Long[] comp){
        return productService.addProduct(name, price, count, componentService.findFiltredComponents(comp));
    }
    @GetMapping("/update")
    public Product update(@RequestParam("id") Long id,
                          @RequestParam("name") String name,
                          @RequestParam("price") Integer price,
                          @RequestParam("count") Integer[] count,
                          @RequestParam("comp") Long[] comp){
        return productService.updateProduct(id, name, price, count, componentService.findFiltredComponents(comp));
    }
    @GetMapping("/remove")
    public Product remove(@RequestParam("id") Long id){
        return productService.deleteProduct(id);
    }
    @GetMapping("/removeAll")
    public void remove(){
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
