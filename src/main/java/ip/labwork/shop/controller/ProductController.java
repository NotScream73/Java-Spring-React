package ip.labwork.shop.controller;

import ip.labwork.WebConfiguration;
import ip.labwork.shop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(WebConfiguration.REST_API + "/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody @Valid ProductDTO productDTO){
        return productService.create(productDTO);
    }
    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id,@RequestBody @Valid ProductDTO productDTO){
        return productService.updateProduct(id, productDTO);
    }
    @DeleteMapping("/{id}")
    public ProductDTO removeProduct(@PathVariable Long id){
        return productService.deleteProduct(id);
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
        return productService.findAllProduct();
    }
}
