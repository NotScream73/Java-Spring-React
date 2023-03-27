package ip.labwork.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderProducts> products;
    public Order(){

    }

    public Order(Date date, Integer price) {
        this.date = date;
        this.price = price;
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

    public void addProduct(OrderProducts orderProducts){
        if  (products == null){
            this.products = new ArrayList<>();
        }
        if (!products.contains(orderProducts))
            this.products.add(orderProducts);
    }
    public void removeProducts(OrderProducts orderProducts){
        if (products.contains(orderProducts))
            this.products.remove(orderProducts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(getId(), order.getId()) && Objects.equals(getDate(), order.getDate()) && Objects.equals(getPrice(), order.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), getPrice());
    }
}
