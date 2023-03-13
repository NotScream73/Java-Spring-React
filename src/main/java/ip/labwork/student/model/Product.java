package ip.labwork.student.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column()
    private String ProductName;
    private Integer Price;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "products_component",
            joinColumns = @JoinColumn(name = "product_fk"),
            inverseJoinColumns = @JoinColumn(name = "component_fk"))
    private List<Component> components;

    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    private List<Order> ords;
    public void addComponent(Component component) {
        if (components == null){
            components = new ArrayList<>();
        }
        this.components.add(component);
        if (component.getProduct() == null) {
            component.setProduct(this);
        }
    }
    public Product() {
    }

    public Product(String ProductName, Integer Price) {
        this.ProductName = ProductName;
        this.Price = Price;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer Price) {
        this.Price = Price;
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
                ", ProductName='" + ProductName + '\'' +
                ", Price='" + Price + '\'' +
                '}';
    }

    public List<Component> getComponents() {
        return components;
    }

    public List<Order> getOrder() {
        /*if(products.contains(product)){
            return true;
        }else{
            return false;
        }*/
        return ords;
    }
    public void setOrder(Order order) {
        if (ords == null){
            ords = new ArrayList<>();
        }
        this.ords.add(order);
        if (!order.getProducts().contains(this)) { // warning this may cause performance issues if you have a large data set since this operation is O(n)
            order.getProducts().add(this);
        }
    }
}
