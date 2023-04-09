package ip.labwork.shop.repository;

import ip.labwork.shop.model.OrderProducts;
import ip.labwork.shop.model.OrderProductsKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProducts, OrderProductsKey> {
    List<OrderProducts> getOrderProductsByOrderId(Long order_id);
}