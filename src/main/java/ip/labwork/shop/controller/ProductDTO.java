package ip.labwork.shop.controller;

import ip.labwork.shop.model.Product;

import java.util.List;
import java.util.Objects;

public class ProductDTO {
    private long id;
    private String name;
    private int price;
    private List<ComponentDTO> componentDTOList;
    private List<OrderDTO> orderDTOList;
    private String image;
    private int count;
    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getProductName();
        this.price = product.getPrice();
        this.image = product.getImage() == null? "" : new String(product.getImage());
        this.componentDTOList = product.getComponents().stream()
                                .filter(x -> Objects.equals(x.getId().getProductId(), product.getId()))
                                .map(y -> new ComponentDTO(y.getComponent(), y.getCount()))
                                .toList();
        this.orderDTOList = product.getOrders() == null ? null : product.getOrders().stream().filter(x -> Objects.equals(x.getId().getProductId(), product.getId())).map(x -> new OrderDTO(x.getOrder())).toList();
    }
    public ProductDTO(Product product, int count) {
        this.id = product.getId();
        this.name = product.getProductName();
        this.price = product.getPrice();
        this.image = product.getImage() == null? "" : new String(product.getImage());
        this.componentDTOList = product.getComponents().stream()
                .filter(x -> Objects.equals(x.getId().getProductId(), product.getId()))
                .map(y -> new ComponentDTO(y.getComponent(), y.getCount()))
                .toList();
        this.count = count;
    }
    public ProductDTO() {
    }

    public long getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public List<ComponentDTO> getComponentDTOList() {
        return componentDTOList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<OrderDTO> getOrderDTOList() {
        return orderDTOList;
    }
}