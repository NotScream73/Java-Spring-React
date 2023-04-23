package ip.labwork.shop.controller;

import ip.labwork.shop.model.Order;
import ip.labwork.shop.model.OrderStatus;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrderDTO {
    private long id;
    @NotBlank(message = "Date can't be null or empty")
    private Date date = new Date();
    @NotBlank(message = "Price can't be null or empty")
    private int price;
    private OrderStatus status = OrderStatus.Неизвестен;
    private List<ProductDTO> productDTOList;
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.date = order.getDate();
        this.price = order.getPrice();
        this.productDTOList = order.getProducts().stream()
                .filter(x -> Objects.equals(x.getId().getOrderId(), order.getId()))
                .map(y -> new ProductDTO(y.getProduct(), y.getCount()))
                .toList();
        this.status = Objects.equals(order.getStatus().toString(), "") ? OrderStatus.Неизвестен : order.getStatus();
    }

    public OrderDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<ProductDTO> getProductDTOList() {
        return productDTOList;
    }

    public void setProductDTOList(List<ProductDTO> productDTOList) {
        this.productDTOList = productDTOList;
    }
}