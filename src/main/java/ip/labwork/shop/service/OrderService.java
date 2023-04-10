package ip.labwork.shop.service;

import ip.labwork.shop.controller.OrderDTO;
import ip.labwork.shop.model.*;
import ip.labwork.shop.repository.OrderProductRepository;
import ip.labwork.shop.repository.OrderRepository;
import ip.labwork.shop.repository.ProductRepository;
import ip.labwork.util.validation.ValidatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final ValidatorUtil validatorUtil;

    public OrderService(OrderRepository orderRepository,
                            ValidatorUtil validatorUtil, OrderProductRepository orderProductRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.validatorUtil = validatorUtil;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
    }
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        int price = 0;
        for(int i = 0; i < orderDTO.getProductDTOList().size(); i++){
            price += orderDTO.getProductDTOList().get(i).getPrice() * orderDTO.getProductDTOList().get(i).getCount();
        }
        final Order order = new Order(new Date(), price, orderDTO.getStatus());
        validatorUtil.validate(order);
        orderRepository.save(order);
        for (int i = 0; i < orderDTO.getProductDTOList().size(); i++) {
            final OrderProducts orderProducts = new OrderProducts(order, productRepository.findById(orderDTO.getProductDTOList().get(i).getId()).orElseThrow(() -> new ProductNotFoundException(1L)), orderDTO.getProductDTOList().get(i).getCount());
            orderProductRepository.saveAndFlush(orderProducts);
            order.addProduct(orderProducts);
            productRepository.findById(orderDTO.getProductDTOList().get(i).getId()).orElseThrow(() -> new ProductNotFoundException(1L)).addOrder(orderProducts);
            orderProductRepository.saveAndFlush(orderProducts);
        }
        return new OrderDTO(findOrder(order.getId()));
    }
    @Transactional
    public Order addOrder(OrderDTO orderDTO) {
        int price = 0;
        for(int i = 0; i < orderDTO.getProductDTOList().size(); i++){
            price += orderDTO.getProductDTOList().get(i).getPrice() * orderDTO.getProductDTOList().get(i).getCount();
        }
        final Order order = new Order(new Date(), price, orderDTO.getStatus());
        orderDTO.setDate(order.getDate());
        orderDTO.setPrice(price);
        validatorUtil.validate(order);
        orderRepository.save(order);
        return order;
    }
    public void addOrderProducts(Order order, Integer[] count, List<Product> products){
        for (int i = 0; i < products.size(); i++) {
            final OrderProducts orderProducts = new OrderProducts(order, products.get(i), count[i]);
            orderProductRepository.saveAndFlush(orderProducts);
            order.addProduct(orderProducts);
            products.get(i).addOrder(orderProducts);
            orderProductRepository.saveAndFlush(orderProducts);
        }
    }
    @Transactional(readOnly = true)
    public Order findOrder(Long id) {
        final Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new OrderNotFoundException(id));
    }
    @Transactional(readOnly = true)
    public List<OrderDTO> findAllOrder() {
        return orderRepository.findAll().stream().map(x -> new OrderDTO(x)).toList();
    }
    @Transactional
    public OrderDTO update(Long id, OrderDTO orderDTO) {
        final Order currentOrder = findOrder(id);
        currentOrder.setDate(orderDTO.getDate());
        currentOrder.setPrice(orderDTO.getPrice());
        validatorUtil.validate(currentOrder);
        orderRepository.save(currentOrder);
        List<OrderProducts> orderProductsList = orderProductRepository.getOrderProductsByOrderId(id);
        List<Long> product_id = new ArrayList<>(orderProductsList.stream().map(p -> p.getId().getProductId()).toList());
        List<Product> newProducts = productRepository.findAllById(orderDTO.getProductDTOList().stream().map(x -> x.getId()).toList());
        for (int i = 0; i < newProducts.size(); i++) {
            final Long currentId = newProducts.get(i).getId();
            if (product_id.contains(currentId)) {
                final OrderProducts orderProducts = orderProductsList.stream().filter(x -> Objects.equals(x.getId().getProductId(), currentId)).toList().get(0);
                orderProductsList.remove(orderProducts);
                int finalI = i;
                product_id = product_id.stream().filter(x -> !Objects.equals(x, newProducts.get(finalI).getId())).toList();
                orderProducts.setCount(orderDTO.getProductDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount());
                orderProductRepository.saveAndFlush(orderProducts);
            }
            else {
                final OrderProducts orderProducts = new OrderProducts(currentOrder, newProducts.get(i), orderDTO.getProductDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount());
                orderProductRepository.saveAndFlush(orderProducts);
                currentOrder.addProduct(orderProducts);
                newProducts.get(i).addOrder(orderProducts);
                orderProductRepository.save(orderProducts);
            }
        }
        for (int i = 0; i < orderProductsList.size(); i++) {
            orderProductsList.get(i).getProduct().removeOrder(orderProductsList.get(i));
            orderProductsList.get(i).getOrder().removeProducts(orderProductsList.get(i));
            orderProductRepository.delete(orderProductsList.get(i));
        }
        return new OrderDTO(currentOrder);
    }
    @Transactional
    public OrderDTO deleteOrder(Long id) {
        final Order currentOrder = findOrder(id);
        int size = currentOrder.getProducts().size();
        for (int i = 0; i < size; i++) {
            OrderProducts temp = currentOrder.getProducts().get(0);
            temp.getProduct().removeOrder(temp);
            temp.getOrder().removeProducts(temp);
            orderProductRepository.delete(temp);
        }
        orderRepository.delete(currentOrder);
        return new OrderDTO(currentOrder);
    }
    @Transactional
    public void deleteAllOrder() {
        orderProductRepository.findAll().forEach(OrderProducts::remove);
        orderProductRepository.deleteAll();
        orderRepository.deleteAll();
    }
}