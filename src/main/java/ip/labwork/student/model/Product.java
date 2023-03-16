package ip.labwork.student.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private Integer price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductComponents> components;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderProducts> orders;


    public Product(){

    }
    public Product(String productName, Integer price, List<ProductComponents> components) {
        this.productName = productName;
        this.price = price;
        this.components = components;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<ProductComponents> getComponents() {
        return components;
    }

    public void setComponents(List<ProductComponents> components) {
        this.components = components;
    }

    public void update(Product product){
        this.productName = product.productName;
        this.price = product.price;
        this.components = product.getComponents();
    }

    public void addComponent(ProductComponents productComponents){
        if  (components == null){
            this.components = new ArrayList<>();
        }
        if (!components.contains(productComponents))
            this.components.add(productComponents);
    }
    public void removeComponent(ProductComponents productComponents){
        if (components.contains(productComponents))
            this.components.remove(productComponents);
    }

    public List<OrderProducts> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderProducts> orders) {
        this.orders = orders;
    }
    public void addOrder(OrderProducts orderProducts){
        if (orders == null){
            orders = new ArrayList<>();
        }
        if (!orders.contains(orderProducts))
            this.orders.add(orderProducts);
    }
    public void removeOrder(OrderProducts orderProducts){
        if (orders.contains(orderProducts))
            this.orders.remove(orderProducts);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

}
