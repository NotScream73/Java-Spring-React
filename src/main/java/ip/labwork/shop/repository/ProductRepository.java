package ip.labwork.shop.repository;

import ip.labwork.shop.model.Product;
import ip.labwork.shop.model.ProductComponents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("Select os from ProductComponents os where os.product.id = :productId")
    List<ProductComponents> getProductComponent(@Param("productId") Long orderId);
}