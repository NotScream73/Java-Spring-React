package ip.labwork.shop.controller;

import ip.labwork.shop.model.Order;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrderDTO {
    private final long id;
    private final Date date;
    private final int price;
    private final List<ProductDTO> productDTOList;
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.date = order.getDate();
        this.price = order.getPrice();
        this.productDTOList = order.getProducts().stream()
                .filter(x -> Objects.equals(x.getId().getOrderId(), order.getId()))
                .map(y -> new ProductDTO(y.getProduct(), y.getCount()))
                .toList();
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public List<ProductDTO> getProductDTOList() {
        return productDTOList;
    }
}