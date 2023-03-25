package ip.labwork.shop.service;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(String.format("Product with id [%s] is not found", id));
    }
}