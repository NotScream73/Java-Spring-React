package ip.labwork.shop.service;

import ip.labwork.shop.model.*;
import ip.labwork.shop.repository.OrderProductRepository;
import ip.labwork.shop.repository.OrderRepository;
import ip.labwork.util.validation.ValidatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ValidatorUtil validatorUtil;

    public OrderService(OrderRepository orderRepository,
                            ValidatorUtil validatorUtil, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.validatorUtil = validatorUtil;
        this.orderProductRepository = orderProductRepository;
    }
    @Transactional
    public Order addOrder(String date, Integer price) {
        Date correctDate = getDate(date);
        final Order order = new Order(correctDate, price);
        validatorUtil.validate(order);
        orderRepository.save(order);
        return order;
    }
    @Transactional
    public void addOrderProducts(Order order, Integer[] count, List<Product> products){
        for (int i = 0; i < products.size(); i++) {
            final OrderProducts orderProducts = new OrderProducts(order, products.get(i), count[i]);
            orderProductRepository.saveAndFlush(orderProducts);
            order.addProduct(orderProducts);
            products.get(i).addOrder(orderProducts);
            orderProductRepository.save(orderProducts);
        }
    }
    public Date getDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        Date newDate;
        try {
            newDate = format.parse(date);
        } catch (Exception exception) {
            newDate = new Date();
        }
        return newDate;
    }

    @Transactional(readOnly = true)
    public Order findOrder(Long id) {
        final Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Order> findAllOrder() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order updateOrder(Long id, String date, Integer price, Integer[] count, List<Product> products) {
        final Order currentOrder = findOrder(id);
        currentOrder.setDate(getDate(date));
        currentOrder.setPrice(price);
        validatorUtil.validate(currentOrder);
        orderRepository.save(currentOrder);
        List<OrderProducts> orderProductsList = orderProductRepository.getOrderProductsByOrderId(id);
        List<Long> product_id = new ArrayList<>(orderProductsList.stream().map(p -> p.getId().getProductId()).toList());
        for (int i = 0; i < products.size(); i++) {
            final Long currentId = products.get(i).getId();
            if (product_id.contains(currentId)) {
                final OrderProducts orderProducts = orderProductsList.stream().filter(x -> Objects.equals(x.getId().getProductId(), currentId)).toList().get(0);
                orderProductsList.remove(orderProducts);
                product_id.remove(products.get(i).getId());
                orderProducts.setCount(count[i]);
                orderProductRepository.save(orderProducts);
            } else {
                final OrderProducts orderProducts = new OrderProducts(currentOrder, products.get(i), count[i]);
                orderProductRepository.saveAndFlush(orderProducts);
                currentOrder.addProduct(orderProducts);
                products.get(i).addOrder(orderProducts);
                orderProductRepository.save(orderProducts);
            }
        }
        for (int i = 0; i < orderProductsList.size(); i++) {
            orderProductsList.get(i).getProduct().removeOrder(orderProductsList.get(i));
            orderProductsList.get(i).getOrder().removeProducts(orderProductsList.get(i));
            orderProductRepository.delete(orderProductsList.get(i));
        }
        return currentOrder;
    }

    /*@Transactional
    public Order updateOrder(Long id, String date, Integer price, Integer[] count, List<Product> products) {
        final Order currentOrder = findOrder(id);
        currentOrder.setDate(getDate(date));
        currentOrder.setPrice(price);
        validatorUtil.validate(currentOrder);
        orderRepository.save(currentOrder);
        List<OrderProducts> orderProductsList = orderProductRepository.getOrderProductsByOrderId(id);
        List<Long> product_id = new ArrayList<>(orderProductsList.stream().map(p -> p.getId().getProductId()).toList());
        for (int i = 0; i < products.size(); i++) {
            final Long currentId = products.get(i).getId();
            if (product_id.contains(currentId)) {
                final OrderProducts orderProducts = orderProductsList.stream().filter(x -> Objects.equals(x.getId().getProductId(), currentId)).toList().get(0);
                orderProductsList.remove(orderProducts);
                product_id.remove(products.get(i).getId());
                orderProducts.setCount(count[i]);
                orderProductRepository.save(orderProducts);
            }
        }
        for (int i = 0; i < orderProductsList.size(); i++) {
            orderProductsList.get(i).getProduct().removeOrder(orderProductsList.get(i));
            orderProductsList.get(i).getOrder().removeProducts(orderProductsList.get(i));
            orderProductRepository.delete(orderProductsList.get(i));
        }
        return currentOrder;
    }
    @Transactional
    public Order update(Order currentOrder, List<OrderProducts> orderProductsList, List<Long> product_id, Integer[] count, List<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            final Long currentId = products.get(i).getId();
            if (product_id.contains(currentId)) {
                orderProductsList.remove(orderProductsList.stream().filter(x -> Objects.equals(x.getId().getProductId(), currentId)).toList().get(0));
                product_id.remove(products.get(i).getId());
            }
            else {
                final OrderProducts orderProducts = new OrderProducts(currentOrder, products.get(i), count[i]);
                currentOrder.addProduct(orderProducts);
                products.get(i).addOrder(orderProducts);
                orderProductRepository.save(orderProducts);
            }
        }
        return currentOrder;
    }*/
    public List<OrderProducts> getOrderProducts(Order currentOrder){
        return orderProductRepository.getOrderProductsByOrderId(currentOrder.getId());
    }
    @Transactional
    public Order deleteOrder(Long id) {
        final Order currentOrder = findOrder(id);
        int size = currentOrder.getProducts().size();
        for (int i = 0; i < size; i++) {
            OrderProducts temp = currentOrder.getProducts().get(0);
            temp.getProduct().removeOrder(temp);
            temp.getOrder().removeProducts(temp);
            orderProductRepository.delete(temp);
        }
        orderRepository.delete(currentOrder);
        return currentOrder;
    }
    @Transactional
    public void deleteAllOrder() {
        orderProductRepository.findAll().forEach(OrderProducts::remove);
        orderProductRepository.deleteAll();
        orderRepository.deleteAll();
    }
}