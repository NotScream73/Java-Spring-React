package ip.labwork.student.service;

import ip.labwork.student.model.Component;
import ip.labwork.student.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Product addProduct(String ProductName, Integer Price, List<Component> components) {
        if (!StringUtils.hasText(ProductName)) {
            throw new IllegalArgumentException("Product is null or empty");
        }
        final Product product = new Product(ProductName, Price);
        for (int i = 0; i < components.size(); i++){
            product.addComponent(components.get(i));
        }
        em.persist(product);
        return product;
    }

    @Transactional(readOnly = true)
    public Product findProduct(Long id) {
        final Product product = em.find(Product.class, id);
        if (product == null) {
            throw new EntityNotFoundException(String.format("Product with id [%s] is not found", id));
        }
        return product;
    }

    @Transactional(readOnly = true)
    public List<Product> findAllProduct() {
        return em.createQuery("select s from Product s", Product.class)
                .getResultList();
    }

    @Transactional
    public Product updateProduct(Long id, String ProductName, Integer Count) {
        if (!StringUtils.hasText(ProductName) || Count != 0) {
            throw new IllegalArgumentException("Product is null or empty");
        }
        final Product currentProduct = findProduct(id);
        currentProduct.setProductName(ProductName);
        currentProduct.setPrice(Count);
        return em.merge(currentProduct);
    }

    @Transactional
    public Product deleteProduct(Long id) {
        final Product currentProduct = findProduct(id);
        em.remove(currentProduct);
        return currentProduct;
    }

    @Transactional
    public void deleteAllProduct() {
        em.createQuery("delete from Product").executeUpdate();
    }
}
