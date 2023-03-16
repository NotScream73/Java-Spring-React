package ip.labwork.student.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private Integer price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductComponents> components = new HashSet<>();

    public Product(){

    }
    public Product(String productName, Integer price, Set<ProductComponents> components) {
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

    public Set<ProductComponents> getComponents() {
        return components;
    }

    public void setComponents(Set<ProductComponents> components) {
        this.components = components;
    }

    public void update(Product product){
        this.productName = product.productName;
        this.price = product.price;
        this.components = product.getComponents();
    }

    public void addComponent(ProductComponents productComponents){
        this.components.add(productComponents);
    }
    public void removeComponent(ProductComponents productComponents){
        this.components.remove(productComponents);
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
