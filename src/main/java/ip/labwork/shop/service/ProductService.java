package ip.labwork.shop.service;

import ip.labwork.shop.controller.ProductDTO;
import ip.labwork.shop.model.Component;
import ip.labwork.shop.model.OrderProducts;
import ip.labwork.shop.model.Product;
import ip.labwork.shop.model.ProductComponents;
import ip.labwork.shop.repository.*;
import ip.labwork.util.validation.ValidatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ProductDTO create(ProductDTO productDTO) {
        final Product product = new Product(productDTO.getName(), productDTO.getPrice(),productDTO.getImage().getBytes());
        validatorUtil.validate(product);
        productRepository.save(product);
        for (int i = 0; i < productDTO.getComponentDTOList().size(); i++) {
            final ProductComponents productComponents = new ProductComponents(componentRepository.findById(productDTO.getComponentDTOList().get(i).getId()).orElseThrow(() -> new ComponentNotFoundException(1L)), product, productDTO.getComponentDTOList().get(i).getCount());
            productComponentRepository.saveAndFlush(productComponents);
            product.addComponent(productComponents);
            componentRepository.findById(productDTO.getComponentDTOList().get(i).getId()).orElseThrow(() -> new ComponentNotFoundException(1L)).addProduct(productComponents);
            productComponentRepository.saveAndFlush(productComponents);
        }
        return new ProductDTO(findProduct(product.getId()));
    }
    @Transactional(readOnly = true)
    public Product findProduct(Long id) {
        final Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAllProduct() {
        return productRepository.findAll().stream().map(x -> new ProductDTO(x)).toList();
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO product) {
        final Product currentProduct = findProduct(id);
        currentProduct.setProductName(product.getName());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setImage(product.getImage().getBytes());
        validatorUtil.validate(currentProduct);
        productRepository.save(currentProduct);
        List<ProductComponents> productComponentsList = productComponentRepository.getProductComponentsByProductId(id);
        List<Long> component_id = new ArrayList<>(productComponentsList.stream().map(p -> p.getId().getComponentId()).toList());
        List<Component> newComponents = componentRepository.findAllById(product.getComponentDTOList().stream().map(x -> x.getId()).toList());
        for (int i = 0; i < newComponents.size(); i++) {
            final Long currentId = newComponents.get(i).getId();
            if (component_id.contains(currentId)) {
                final ProductComponents productComponents = productComponentsList.stream().filter(x -> Objects.equals(x.getId().getComponentId(), currentId)).toList().get(0);
                productComponentsList.remove(productComponents);
                int finalI = i;
                component_id = component_id.stream().filter(x -> !Objects.equals(x, newComponents.get(finalI).getId())).toList();
                productComponents.setCount(product.getComponentDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount());
                productComponentRepository.saveAndFlush(productComponents);
            }
            else {
                final ProductComponents productComponents = new ProductComponents(newComponents.get(i), currentProduct, product.getComponentDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount());
                productComponentRepository.saveAndFlush(productComponents);
                currentProduct.addComponent(productComponents);
                newComponents.get(i).addProduct(productComponents);
                productComponentRepository.save(productComponents);
            }
        }
        for (int i = 0; i < productComponentsList.size(); i++) {
            productComponentsList.get(i).getComponent().removeProduct(productComponentsList.get(i));
            productComponentsList.get(i).getProduct().removeComponent(productComponentsList.get(i));
            productComponentRepository.delete(productComponentsList.get(i));
        }
        return new ProductDTO(currentProduct);
    }
    @Transactional
    public ProductDTO deleteProduct(Long id) {
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
            OrderProducts orderProducts = currentProduct.getOrders().get(0);
            orderProducts.getProduct().removeOrder(orderProducts);
            orderProducts.getOrder().removeProducts(orderProducts);
            orderProductRepository.delete(orderProducts);
        }
        productRepository.delete(currentProduct);
        return new ProductDTO(currentProduct);
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
