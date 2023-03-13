package ip.labwork.student.service;

import ip.labwork.student.model.Order;
import ip.labwork.student.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class OrdService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Order addOrd(Date CreateDate, Integer Price, ArrayList<Product> products) {
        if (!StringUtils.hasText(CreateDate.toString())) {
            throw new IllegalArgumentException("Ord is null or empty");
        }
        final Order order = new Order(new Date(), Price);
        for (int i = 0 ; i < products.size(); i++){
            order.addProduct(products.get(i));
        }
        em.persist(order);
        return order;
    }

    @Transactional(readOnly = true)
    public Order findOrd(Long id) {
        final Order product = em.find(Order.class, id);
        if (product == null) {
            throw new EntityNotFoundException(String.format("Ord with id [%s] is not found", id));
        }
        return product;
    }

    @Transactional(readOnly = true)
    public List<Order> findAllOrd() {
        return em.createQuery("select s from Order s", Order.class)
                .getResultList();
    }

    @Transactional
    public Order updateOrd(Long id, Date CreateDate, Integer Count) {
        if (!StringUtils.hasText(CreateDate.toString())) {
            throw new IllegalArgumentException("Ord is null or empty");
        }
        final Order currentOrd = findOrd(id);
        currentOrd.setCreateDate(CreateDate);
        currentOrd.setCount(Count);
        return em.merge(currentOrd);
    }

    @Transactional
    public Order deleteOrd(Long id) {
        final Order currentOrd = findOrd(id);
        em.remove(currentOrd);
        return currentOrd;
    }

    @Transactional
    public void deleteAllOrd() {
        em.createQuery("delete from Order").executeUpdate();
    }
}
