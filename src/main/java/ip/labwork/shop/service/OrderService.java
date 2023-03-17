package ip.labwork.shop.service;

import ip.labwork.shop.model.Order;
import ip.labwork.shop.model.OrderProducts;
import ip.labwork.shop.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Order addOrder(String date, Integer price, Integer[] count, List<Product> products) {
        if (!StringUtils.hasText(date) || price == 0 || count.length == 0 || Arrays.stream(count).filter(c -> c == 0).toList().size() != 0 || products.size() == 0 || products.stream().filter(Objects::isNull).toList().size() != 0 || count.length != products.size()) {
            throw new IllegalArgumentException("Order is null or empty");
        }
        Date correctDate = getDate(date);
        final Order order = new Order(correctDate, price);
        em.persist(order);
        for (int i = 0; i < products.size(); i++) {
            final OrderProducts orderProducts = new OrderProducts(order, products.get(i), count[i]);
            order.addProduct(orderProducts);
            products.get(i).addOrder(orderProducts);
            em.persist(orderProducts);
        }
        return order;
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
        final Order order = em.find(Order.class, id);
        if (order == null) {
            throw new EntityNotFoundException(String.format("Order with id [%s] is not found", id));
        }
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> findAllOrder() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    @Transactional
    public Order updateOrder(Long id, String date, Integer price, Integer[] count, List<Product> products) {
        if (!StringUtils.hasText(date) || price == 0 || count.length == 0 || Arrays.stream(count).filter(c -> c == 0).toList().size() != 0 || products.size() == 0 || products.stream().filter(Objects::isNull).toList().size() != 0 || count.length != products.size()) {
            throw new IllegalArgumentException("Order is null or empty");
        }
        final Order currentOrder = findOrder(id);
        currentOrder.setDate(getDate(date));
        currentOrder.setPrice(price);
        em.merge(currentOrder);
        List<OrderProducts> orderProductsList = em.createQuery("select o from OrderProducts o where o.id.orderId = " + id, OrderProducts.class)
                .getResultList();
        List<Long> product_id = new ArrayList<>(orderProductsList.stream().map(p -> p.getId().getProductId()).toList());
        for (int i = 0; i < products.size(); i++) {
            final Long currentId = products.get(i).getId();
            if (product_id.contains(currentId)) {
                final OrderProducts orderProducts = orderProductsList.stream().filter(x -> Objects.equals(x.getId().getProductId(), currentId)).toList().get(0);
                orderProductsList.remove(orderProducts);
                product_id.remove(products.get(i).getId());
                orderProducts.setCount(count[i]);
                em.merge(orderProducts);
            } else {
                final OrderProducts orderProducts = new OrderProducts(currentOrder, products.get(i), count[i]);
                currentOrder.addProduct(orderProducts);
                products.get(i).addOrder(orderProducts);
                em.persist(orderProducts);
            }
        }
        for (int i = 0; i < orderProductsList.size(); i++) {
            orderProductsList.get(i).getProduct().removeOrder(orderProductsList.get(i));
            orderProductsList.get(i).getOrder().removeProducts(orderProductsList.get(i));
            em.remove(orderProductsList.get(i));
        }
        return currentOrder;
    }

    @Transactional
    public Order deleteOrder(Long id) {
        final Order currentOrder = findOrder(id);
        int size = currentOrder.getProducts().size();
        for (int i = 0; i < size; i++) {
            OrderProducts temp = currentOrder.getProducts().get(0);
            temp.getProduct().removeOrder(temp);
            temp.getOrder().removeProducts(temp);
            em.remove(temp);
        }
        em.remove(currentOrder);
        return currentOrder;
    }
    @Transactional
    public void deleteAllOrder() {
        em.createQuery("delete from OrderProducts").executeUpdate();
        em.createQuery("delete from Order").executeUpdate();
    }
}