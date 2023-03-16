package ip.labwork.student.service;

import ip.labwork.student.model.Component;
import ip.labwork.student.model.Product;
import ip.labwork.student.model.ProductComponents;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Product addProduct(String productName, Integer price, Set<ProductComponents> components){
        Product product = new Product(productName,price,components);
        Product newProduct = new Product();
        newProduct.setProductName(product.getProductName());
        newProduct.setPrice(product.getPrice());
        newProduct.getComponents().addAll((product.getComponents()
                .stream()
                .map(productComponents -> {
                    Component component = em.find(Component.class, productComponents.getComponent().getId());
                    ProductComponents newProductComponents = new ProductComponents();
                    newProductComponents.setComponent(component);
                    newProductComponents.setProduct(newProduct);
                    newProductComponents.setCount(productComponents.getCount());
                    return newProductComponents;
                })
                .collect(Collectors.toSet())
        ));
        em.persist(newProduct);
        return newProduct;
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
    public void check(){
        int s = 5;
    }
    @Transactional
    public void deleteAllProduct() {
        em.createQuery("delete from Product").executeUpdate();
    }
}
