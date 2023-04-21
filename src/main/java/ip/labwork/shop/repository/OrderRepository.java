package ip.labwork.shop.repository;

import ip.labwork.shop.model.Order;
import ip.labwork.shop.model.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("Select os from OrderProducts os where os.order.id = :orderId")
    List<OrderProducts> getOrderProduct(@Param("orderId") Long orderId);
}