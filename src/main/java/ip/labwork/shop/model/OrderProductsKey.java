package ip.labwork.shop.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderProductsKey implements Serializable {
    private Long productId;
    private Long orderId;

    public OrderProductsKey() {
    }

    public OrderProductsKey(Long productId, Long orderId) {
        this.productId = productId;
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderProductsKey that)) return false;
        return Objects.equals(getProductId(), that.getProductId()) && Objects.equals(getOrderId(), that.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getOrderId());
    }
}