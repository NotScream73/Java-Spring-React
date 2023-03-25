package ip.labwork.shop.repository;

import ip.labwork.shop.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<Component, Long> {
}
