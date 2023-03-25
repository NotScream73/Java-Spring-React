package ip.labwork.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "order_product")
public class OrderProducts {
    @EmbeddedId
    private OrderProductsKey id;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
    @NotBlank(message = "Count can't be null or empty")
    @Column(name = "count")
    private Integer count;

    public OrderProducts() {
    }

    public OrderProducts(Order order, Product product, Integer count) {
        this.order = order;
        this.id = new OrderProductsKey(product.getId(), order.getId());
        this.id.setOrderId(order.getId());
        this.id.setProductId(product.getId());
        this.product = product;
        this.count = count;
    }

    public OrderProductsKey getId() {
        return id;
    }

    public void setId(OrderProductsKey id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
