package ip.labwork.shop.controller;

import ip.labwork.shop.service.ProductService;
import ip.labwork.shop.model.Product;
import ip.labwork.shop.service.ComponentService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ProductDTO createProduct(@RequestParam("name") String name,
                          @RequestParam("price") Integer price,
                          @RequestParam("count") Integer[] count,
                          @RequestParam("comp") Long[] comp){
        return new ProductDTO(productService.addProduct(name, price, count, componentService.findFiltredComponents(comp)));
    }
    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id,
                          @RequestParam("name") String name,
                          @RequestParam("price") Integer price,
                          @RequestParam("count") Integer[] count,
                          @RequestParam("comp") Long[] comp){
        return new ProductDTO(productService.updateProduct(id, name, price, count, componentService.findFiltredComponents(comp)));
    }
    @DeleteMapping("/{id}")
    public ProductDTO removeProduct(@PathVariable Long id){
        ProductDTO temp = new ProductDTO(productService.deleteProduct(id));
        productService.test();
        return null;
    }
    @DeleteMapping
    public void removeAllProduct(){
        productService.deleteAllProduct();
    }
    @GetMapping("/{id}")
    public ProductDTO findProduct(@PathVariable Long id){
        return new ProductDTO(productService.findProduct(id));
    }
    @GetMapping
    public List<ProductDTO> findAllProduct(){
        return productService.findAllProduct().stream()
                .map(ProductDTO::new)
                .toList();
    }
}
