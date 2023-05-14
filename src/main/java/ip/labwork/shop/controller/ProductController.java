package ip.labwork.shop.controller;

import ip.labwork.shop.service.ProductService;
import ip.labwork.user.model.UserRole;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Secured({UserRole.AsString.ADMIN})
    public ProductDTO createProduct(@RequestBody @Valid ProductDTO productDTO) {
        return productService.create(productDTO);
    }

    @PutMapping("/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public ProductDTO removeProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @DeleteMapping
    @Secured({UserRole.AsString.ADMIN})
    public void removeAllProduct() {
        productService.deleteAllProduct();
    }

    @GetMapping("/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public ProductDTO findProduct(@PathVariable Long id) {
        return new ProductDTO(productService.findProduct(id));
    }

    @GetMapping
    @Secured({UserRole.AsString.ADMIN})
    public List<ProductDTO> findAllProduct() {
        return productService.findAllProduct();
    }
}
