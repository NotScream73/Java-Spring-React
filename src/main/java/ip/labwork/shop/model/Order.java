package ip.labwork.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //@NotNull(message = "Date can't be null or empty")
    @Column(name = "date")
    private Date date;
    @NotNull(message = "Price can't be null or empty")
    @Column(name = "price")
    private Integer price;
    private Long user_id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderProducts> products;
    private OrderStatus status;

    public Order() {

    }

    public Order(Date date, Integer price, OrderStatus status, Long user_id) {
        this.date = date;
        this.price = price;
        this.user_id = user_id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<OrderProducts> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProducts> products) {
        this.products = products;
    }

    public void addProduct(OrderProducts orderProducts) {
        if (products == null) {
            this.products = new ArrayList<>();
        }
        if (!products.contains(orderProducts))
            this.products.add(orderProducts);
    }

    public void removeProducts(OrderProducts orderProducts) {
        if (products.contains(orderProducts))
            this.products.remove(orderProducts);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(getId(), order.getId()) && Objects.equals(getDate(), order.getDate()) && Objects.equals(getPrice(), order.getPrice()) && Objects.equals(getUser_id(), order.getUser_id()) && Objects.equals(getProducts(), order.getProducts()) && getStatus() == order.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getPrice());
    }
}
