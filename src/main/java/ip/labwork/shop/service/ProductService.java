package ip.labwork.shop.service;

import ip.labwork.shop.controller.ComponentDTO;
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
    private final ComponentRepository componentRepository;
    private final ValidatorUtil validatorUtil;

    public ProductService(ProductRepository productRepository,
                            ValidatorUtil validatorUtil, ComponentRepository componentRepository) {
        this.productRepository = productRepository;
        this.validatorUtil = validatorUtil;
        this.componentRepository = componentRepository;
    }

    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        final Product product = new Product(productDTO.getName(), productDTO.getPrice(),productDTO.getImage().getBytes());
        validatorUtil.validate(product);
        productRepository.save(product);
        if(productDTO.getComponentDTOList() != null){
            for (int i = 0; i < productDTO.getComponentDTOList().size(); i++) {
                final ProductComponents productComponents = new ProductComponents(componentRepository.findById(productDTO.getComponentDTOList().get(i).getId()).orElseThrow(() -> new ComponentNotFoundException(1L)), product, productDTO.getComponentDTOList().get(i).getCount());
                product.addComponent(productComponents);
            }
        }
        productRepository.save(product);
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
        List<ProductComponents> productComponentsList = productRepository.getProductComponent(id);
        List<Long> component_id = new ArrayList<>(productComponentsList.stream().map(p -> p.getId().getComponentId()).toList());
        List<Component> newComponents = componentRepository.findAllById(product.getComponentDTOList().stream().map(x -> x.getId()).toList());
        for (int i = 0; i < newComponents.size(); i++) {
            final Long currentId = newComponents.get(i).getId();
            if (component_id.contains(currentId)) {
                final ProductComponents productComponents = productComponentsList.stream().filter(x -> x.getId().getComponentId().equals(currentId)).findFirst().get();
                productComponentsList.remove(productComponents);
                currentProduct.removeComponent(productComponents);
                currentProduct.addComponent(new ProductComponents(newComponents.get(i) , currentProduct, product.getComponentDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount()));
                int finalI = i;
                component_id = component_id.stream().filter(x -> !Objects.equals(x, newComponents.get(finalI).getId())).toList();
                productComponents.setCount(product.getComponentDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount());
                productRepository.saveAndFlush(currentProduct);
            }
            else {
                final ProductComponents productComponents = new ProductComponents(newComponents.get(i), currentProduct, product.getComponentDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount());
                currentProduct.addComponent(productComponents);
                productRepository.saveAndFlush(currentProduct);
            }
        }
        for (int i = 0; i < productComponentsList.size(); i++) {
            productComponentsList.get(i).getComponent().removeProduct(productComponentsList.get(i));
            productComponentsList.get(i).getProduct().removeComponent(productComponentsList.get(i));
        }
        productRepository.saveAndFlush(currentProduct);
        return new ProductDTO(currentProduct);
    }
    @Transactional
    public ProductDTO updateFields(Long id, ProductDTO product) {
        final Product currentProduct = findProduct(id);
        currentProduct.setProductName(product.getName());
        if (product.getImage().length()>23){
            currentProduct.setImage(product.getImage().getBytes());
        }
        validatorUtil.validate(currentProduct);
        productRepository.save(currentProduct);
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
        }
        int ordSize = currentProduct.getOrders().size();
        for (int i = 0; i < ordSize; i++){
            OrderProducts orderProducts = currentProduct.getOrders().get(0);
            orderProducts.getProduct().removeOrder(orderProducts);
            orderProducts.getOrder().removeProducts(orderProducts);
        }
        productRepository.delete(currentProduct);
        return new ProductDTO(currentProduct);
    }

    @Transactional
    public void deleteAllProduct() {
        productRepository.deleteAll();
    }

    @Transactional
    public ProductDTO addComponent(Long id, ComponentDTO componentDTO) {
        if (componentDTO.getCount() <= 0){
            return null;
        }
        final Product currentProduct = findProduct(id);
        List<ProductComponents> productComponentsList = productRepository.getProductComponent(id);
        if(productComponentsList.stream().filter(x -> x.getId().getComponentId() == componentDTO.getId()).toList().size() != 0) {
            final ProductComponents productComponents = productRepository.getProductComponent(id).stream().filter(x -> x.getId().getComponentId().equals(componentDTO.getId())).findFirst().get();
            currentProduct.removeComponent(productComponents);
            currentProduct.addComponent(new ProductComponents(componentRepository.findById(componentDTO.getId()).orElseThrow(() -> new ComponentNotFoundException(id)), currentProduct, componentDTO.getCount()));
        }else{
            final ProductComponents productComponents = new ProductComponents(componentRepository.findById(componentDTO.getId()).orElseThrow(() -> new ComponentNotFoundException(id)), currentProduct, componentDTO.getCount());
            currentProduct.addComponent(productComponents);
        }
        productRepository.saveAndFlush(currentProduct);
        int price = 0;
        for(int i = 0; i < productRepository.getProductComponent(id).size(); i++){
            price += productRepository.getProductComponent(id).get(i).getComponent().getPrice() * productRepository.getProductComponent(id).get(i).getCount();
        }
        currentProduct.setPrice(price);
        productRepository.saveAndFlush(currentProduct);
        return new ProductDTO(currentProduct);
    }
    @Transactional
    public ProductDTO deleteComponent(Long id, Long componentId) {
        Product currentProduct = findProduct(id);
        final ProductComponents productComponents = productRepository.getProductComponent(id).stream().filter(x -> x.getId().getComponentId().equals(componentId)).findFirst().get();
        currentProduct.removeComponent(productComponents);
        Component component = componentRepository.findById(componentId).orElseThrow(() -> new ComponentNotFoundException(componentId));
        component.removeProduct(productComponents);
        productRepository.save(currentProduct);
        currentProduct = findProduct(id);
        int price = 0;
        for(int i = 0; i < currentProduct.getComponents().size(); i++){
            price += currentProduct.getComponents().get(i).getComponent().getPrice() * currentProduct.getComponents().get(i).getCount();
        }
        currentProduct.setPrice(price);
        productRepository.saveAndFlush(currentProduct);
        return new ProductDTO(currentProduct);
    }
}
