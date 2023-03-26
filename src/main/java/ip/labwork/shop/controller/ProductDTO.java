package ip.labwork.shop.controller;

import ip.labwork.shop.model.Product;
import ip.labwork.shop.model.ProductComponents;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductDTO {
    private final long id;
    private final String productName;
    private final int price;
    private final List<ComponentDTO> componentDTOList;
    private final List<OrderDTO> orderDTOList;
    public ProductDTO(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.componentDTOList = product.getComponents().stream()
                                .filter(x -> Objects.equals(x.getId().getProductId(), product.getId()))
                                .map(y -> new ComponentDTO(y.getComponent(), y.getCount()))
                                .toList();
        this.orderDTOList = product.getOrders() == null ? null : product.getOrders().stream().filter(x -> Objects.equals(x.getId().getProductId(), product.getId())).map(x -> new OrderDTO(x.getOrder())).toList();
    }

    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public List<ComponentDTO> getComponentDTOList() {
        return componentDTOList;
    }

    public List<OrderDTO> getOrderDTOList() {
        return orderDTOList;
    }
}