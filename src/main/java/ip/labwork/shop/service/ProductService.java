package ip.labwork.shop.service;

import ip.labwork.shop.model.Component;
import ip.labwork.shop.model.OrderProducts;
import ip.labwork.shop.model.Product;
import ip.labwork.shop.model.ProductComponents;
import ip.labwork.shop.repository.ComponentRepository;
import ip.labwork.shop.repository.OrderProductRepository;
import ip.labwork.shop.repository.ProductComponentRepository;
import ip.labwork.shop.repository.ProductRepository;
import ip.labwork.util.validation.ValidatorUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductComponentRepository productComponentRepository;
    private final OrderProductRepository orderProductRepository;
    private final ComponentRepository componentRepository;
    private final ValidatorUtil validatorUtil;

    public ProductService(ProductRepository productRepository,
                            ValidatorUtil validatorUtil, ProductComponentRepository productComponentRepository, OrderProductRepository orderProductRepository, ComponentRepository componentRepository) {
        this.productRepository = productRepository;
        this.validatorUtil = validatorUtil;
        this.productComponentRepository = productComponentRepository;
        this.orderProductRepository = orderProductRepository;
        this.componentRepository = componentRepository;
    }
    @Transactional
    public Product addProduct(String productName, Integer price) {
        final Product product = new Product(productName, price);
        validatorUtil.validate(product);
        productRepository.save(product);
        return product;
    }
    @Transactional
    public void addProductComponents(Product product, Integer[] count, List<Component> components){
        for (int i = 0; i < components.size(); i++) {
            final ProductComponents productComponents = new ProductComponents(components.get(i), product, count[i]);
            product.addComponent(productComponents);
            components.get(i).addProduct(productComponents);
            productComponentRepository.saveAndFlush(productComponents);
        }
    }
    @Transactional(readOnly = true)
    public Product findProduct(Long id) {
        final Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    /*@Transactional
    public Product updateProduct(Long id, String productName, Integer price, Integer[] count, List<Component> components) {
        final Product currentProduct = findProduct(id);
        currentProduct.setProductName(productName);
        currentProduct.setPrice(price);
        validatorUtil.validate(currentProduct);
        productRepository.save(currentProduct);
        List<ProductComponents> productComponentsList = productComponentRepository.getProductComponentsByProductId(id);
        List<Long> component_id = new ArrayList<>(productComponentsList.stream().map(p -> p.getId().getComponentId()).toList());
        for (int i = 0; i < components.size(); i++) {
            final Long currentId = components.get(i).getId();
            if (component_id.contains(currentId)) {
                final ProductComponents productComponents = productComponentsList.stream().filter(x -> Objects.equals(x.getId().getComponentId(), currentId)).toList().get(0);
                productComponentsList.remove(productComponents);
                component_id.remove(components.get(i).getId());
                productComponents.setCount(count[i]);
                productComponentRepository.saveAndFlush(productComponents);
            }
            else {
                final ProductComponents productComponents = new ProductComponents(components.get(i), currentProduct, count[i]);
                currentProduct.addComponent(productComponents);
                components.get(i).addProduct(productComponents);
                productComponentRepository.save(productComponents);
            }
        }
        for (int i = 0; i < productComponentsList.size(); i++) {
            productComponentsList.get(i).getComponent().removeProduct(productComponentsList.get(i));
            productComponentsList.get(i).getProduct().removeComponent(productComponentsList.get(i));
            productComponentRepository.delete(productComponentsList.get(i));
        }
        return currentProduct;
    }*/
    @Transactional
    public Product updateProduct(Long id, String productName, Integer price, Integer[] count, List<Component> components) {
        final Product currentProduct = findProduct(id);
        currentProduct.setProductName(productName);
        currentProduct.setPrice(price);
        validatorUtil.validate(currentProduct);
        productRepository.save(currentProduct);
        List<ProductComponents> productComponentsList = productComponentRepository.getProductComponentsByProductId(id);
        List<Long> component_id = new ArrayList<>(productComponentsList.stream().map(p -> p.getId().getComponentId()).toList());
        for (int i = 0; i < components.size(); i++) {
            final Long currentId = components.get(i).getId();
            if (component_id.contains(currentId)) {
                final ProductComponents productComponents = productComponentsList.stream().filter(x -> Objects.equals(x.getId().getComponentId(), currentId)).toList().get(0);
                productComponentsList.remove(productComponents);
                component_id.remove(components.get(i).getId());
                productComponents.setCount(count[i]);
                productComponentRepository.saveAndFlush(productComponents);
            }
        }
        for (int i = 0; i < productComponentsList.size(); i++) {
            productComponentsList.get(i).getComponent().removeProduct(productComponentsList.get(i));
            productComponentsList.get(i).getProduct().removeComponent(productComponentsList.get(i));
            productComponentRepository.delete(productComponentsList.get(i));
        }
        return currentProduct;
    }
    @Transactional
    public Product update(Product currentProduct, List<ProductComponents> productComponentsList, List<Long> component_id, Integer[] count, List<Component> components) {
        for (int i = 0; i < components.size(); i++) {
            final Long currentId = components.get(i).getId();
            if (component_id.contains(currentId)) {
                productComponentsList.remove(productComponentsList.stream().filter(x -> Objects.equals(x.getId().getComponentId(), currentId)).toList().get(0));
                component_id.remove(components.get(i).getId());
            }
            else {
                final ProductComponents productComponents = new ProductComponents(components.get(i), currentProduct, count[i]);
                currentProduct.addComponent(productComponents);
                components.get(i).addProduct(productComponents);
                productComponentRepository.save(productComponents);
            }
        }
        return currentProduct;
    }
    public List<ProductComponents> getProductComponents(Product currentProduct){
        return productComponentRepository.getProductComponentsByProductId(currentProduct.getId());
    }
    public void test(){
        int s =5;
    }
    @Transactional
    public Product deleteProduct(Long id) {
        final Product currentProduct = findProduct(id);
        int size = currentProduct.getComponents().size();
        for (int i = 0; i < size; i++) {
            ProductComponents temp = currentProduct.getComponents().get(0);
            temp.getComponent().removeProduct(temp);
            temp.getProduct().removeComponent(temp);
            productComponentRepository.delete(temp);
        }
        int ordSize = currentProduct.getOrders().size();
        for (int i = 0; i < ordSize; i++){
            OrderProducts temp = currentProduct.getOrders().get(0);
            temp.getProduct().removeOrder(temp);
            temp.getOrder().removeProducts(temp);
            orderProductRepository.delete(temp);
        }
        productRepository.delete(currentProduct);
        return currentProduct;
    }

    @Transactional
    public void deleteAllProduct() {
        orderProductRepository.findAll().forEach(OrderProducts::remove);
        productComponentRepository.findAll().forEach(ProductComponents::remove);
        productComponentRepository.deleteAll();
        orderProductRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Transactional
    public List<Product> findFiltredProducts(Long[] arr) {
        return productRepository.findAllById(Arrays.stream(arr).toList());
    }
}
