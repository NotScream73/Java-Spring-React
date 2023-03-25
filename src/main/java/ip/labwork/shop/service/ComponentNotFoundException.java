package ip.labwork.shop.service;

public class ComponentNotFoundException extends RuntimeException {
    public ComponentNotFoundException(Long id) {
        super(String.format("Component with id [%s] is not found", id));
    }
}
