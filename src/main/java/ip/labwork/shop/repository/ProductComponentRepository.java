package ip.labwork.shop.repository;

import ip.labwork.shop.model.ProductComponents;
import ip.labwork.shop.model.ProductComponentsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductComponentRepository extends JpaRepository<ProductComponents, ProductComponentsKey> {
    List<ProductComponents> getProductComponentsByProductId(Long product_id);
}