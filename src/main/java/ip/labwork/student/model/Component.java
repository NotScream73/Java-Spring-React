package ip.labwork.student.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column()
    private String componentName;
    private Integer price;

    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ProductComponents> products;
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

    public Set<ProductComponents> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductComponents> products) {
        this.products = products;
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