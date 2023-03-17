package ip.labwork.shop.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductComponentsKey implements Serializable {
    private Long productId;
    private Long componentId;

    public ProductComponentsKey() {
    }

    public ProductComponentsKey(Long productId, Long componentId) {
        this.productId = productId;
        this.componentId = componentId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductComponentsKey that)) return false;
        return Objects.equals(getProductId(), that.getProductId()) && Objects.equals(getComponentId(), that.getComponentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getComponentId());
    }
}
