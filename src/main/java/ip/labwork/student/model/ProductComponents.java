package ip.labwork.student.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class ProductComponents {
    @EmbeddedId
    private ProductComponentsKey id;
    @ManyToOne
    @MapsId("componentId")
    @JoinColumn(name = "component_id")
    private Component component;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    private Integer count;
    public ProductComponents() {
    }

    public ProductComponents(Component component, Product product, Integer count) {
        this.component = component;
        this.id.setComponentId(component.getId());
        this.id.setProductId(product.getId());
        this.product = product;
        this.count = count;
    }

    public ProductComponentsKey getId() {
        return id;
    }

    public void setId(ProductComponentsKey id) {
        this.id = id;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
