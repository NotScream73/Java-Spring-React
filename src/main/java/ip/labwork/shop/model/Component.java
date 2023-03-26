package ip.labwork.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "components")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "ComponentName can't be null or empty")
    @Column(name = "name")
    private String componentName;
    @NotNull(message= "Price may not be empty")
    @Column(name = "price")
    private Integer price;

    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ProductComponents> products;

    public Component() {
    }

    public Component(String componentName, Integer price) {
        this.componentName = componentName;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<ProductComponents> getProducts() {
        return products;
    }

    public void setProducts(List<ProductComponents> products) {
        this.products = products;
    }

    public void addProduct(ProductComponents productComponents) {
        if (products == null) {
            products = new ArrayList<>();
        }
        if (!products.contains(productComponents))
            this.products.add(productComponents);
    }

    public void removeProduct(ProductComponents productComponents) {
        if (products.contains(productComponents))
            this.products.remove(productComponents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(id, component.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", componentName='" + componentName + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}