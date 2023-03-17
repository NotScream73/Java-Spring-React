package ip.labwork.shop.service;

import ip.labwork.shop.model.Component;
import ip.labwork.shop.model.OrderProducts;
import ip.labwork.shop.model.Product;
import ip.labwork.shop.model.ProductComponents;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Product addProduct(String productName, Integer price, Integer[] count, List<Component> components) {
        if (!StringUtils.hasText(productName) || price == 0 || count.length == 0 || Arrays.stream(count).filter(c -> c == 0).toList().size() != 0 || components.size() == 0 || components.stream().filter(Objects::isNull).toList().size() != 0 || count.length != components.size()) {
            throw new IllegalArgumentException("Product name is null or empty");
        }
        final Product product = new Product(productName, price);
        em.persist(product);
        for (int i = 0; i < components.size(); i++) {
            final ProductComponents productComponents = new ProductComponents(components.get(i), product, count[i]);
            product.addComponent(productComponents);
            components.get(i).addProduct(productComponents);
            em.persist(productComponents);
        }
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
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    @Transactional
    public Product updateProduct(Long id, String productName, Integer price, Integer[] count, List<Component> components) {
        if (!StringUtils.hasText(productName) || price == 0 || count.length == 0 || Arrays.stream(count).filter(c -> c == 0).toList().size() != 0 || components.size() == 0 || components.stream().filter(Objects::isNull).toList().size() != 0 || count.length != components.size()) {
            throw new IllegalArgumentException("Product name is null or empty");
        }
        final Product currentProduct = findProduct(id);
        currentProduct.setProductName(productName);
        currentProduct.setPrice(price);
        em.merge(currentProduct);
        List<ProductComponents> productComponentsList = em.createQuery("select p from ProductComponents p where p.id.productId = " + id, ProductComponents.class)
                .getResultList();
        List<Long> component_id = new ArrayList<>(productComponentsList.stream().map(p -> p.getId().getComponentId()).toList());
        for (int i = 0; i < components.size(); i++) {
            final Long currentId = components.get(i).getId();
            if (component_id.contains(currentId)) {
                final ProductComponents productComponents = productComponentsList.stream().filter(x -> Objects.equals(x.getId().getComponentId(), currentId)).toList().get(0);
                productComponentsList.remove(productComponents);
                component_id.remove(components.get(i).getId());
                productComponents.setCount(count[i]);
                em.merge(productComponents);
            } else {
                final ProductComponents productComponents = new ProductComponents(components.get(i), currentProduct, count[i]);
                currentProduct.addComponent(productComponents);
                components.get(i).addProduct(productComponents);
                em.persist(productComponents);
            }
        }
        for (int i = 0; i < productComponentsList.size(); i++) {
            productComponentsList.get(i).getComponent().removeProduct(productComponentsList.get(i));
            productComponentsList.get(i).getProduct().removeComponent(productComponentsList.get(i));
            em.remove(productComponentsList.get(i));
        }
        return currentProduct;
    }

    @Transactional
    public Product deleteProduct(Long id) {
        final Product currentProduct = findProduct(id);
        int size = currentProduct.getComponents().size();
        for (int i = 0; i < size; i++) {
            ProductComponents temp = currentProduct.getComponents().get(0);
            temp.getComponent().removeProduct(temp);
            temp.getProduct().removeComponent(temp);
            em.remove(temp);
        }
        int ordSize = currentProduct.getOrders().size();
        for (int i = 0; i < ordSize; i++){
            OrderProducts temp = currentProduct.getOrders().get(0);
            temp.getProduct().removeOrder(temp);
            temp.getOrder().removeProducts(temp);
            em.remove(temp);
        }
        em.remove(currentProduct);
        return currentProduct;
    }

    @Transactional
    public void deleteAllProduct() {
        em.createQuery("delete from ProductComponents").executeUpdate();
        em.createQuery("delete from OrderProducts ").executeUpdate();
        em.createQuery("delete from Product").executeUpdate();
    }

    @Transactional
    public List<Product> findFiltredProducts(Long[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array id is empty");
        }
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            productList.add(em.find(Product.class, arr[i]));
        }
        return productList;
    }
}
