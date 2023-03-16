package ip.labwork.student.service;

import ip.labwork.student.model.Order;
import ip.labwork.student.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Order addOrder(Date date, Integer price){
        Order order = new Order();
        order.setDate(date);
        order.setPrice(price);
        em.persist(order);
        return order;
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
    public Order updateOrder(Long id, Date date, Integer price) {
        final Order currentOrder = findOrder(id);
        currentOrder.setDate(date);
        currentOrder.setPrice(price);
        return em.merge(currentOrder);
    }

    @Transactional
    public Order deleteOrder(Long id) {
        final Order currentOrder = findOrder(id);
        em.remove(currentOrder);
        return currentOrder;
    }
    @Transactional
    public void deleteAllOrder() {
        em.createQuery("delete from Order").executeUpdate();
    }
}