package ip.labwork.shop.service;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super(String.format("Order with id [%s] is not found", id));
    }
}