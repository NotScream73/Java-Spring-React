package ip.labwork.student.service;

import ip.labwork.student.model.Ord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
@Service
public class OrdService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Ord addOrd(Date CreateDate, Integer Price) {
        if (!StringUtils.hasText(CreateDate.toString())) {
            throw new IllegalArgumentException("Ord is null or empty");
        }
        final Ord order = new Ord(new Date(), Price);
        em.persist(order);
        return order;
    }

    @Transactional(readOnly = true)
    public Ord findOrd(Long id) {
        final Ord product = em.find(Ord.class, id);
        if (product == null) {
            throw new EntityNotFoundException(String.format("Ord with id [%s] is not found", id));
        }
        return product;
    }

    @Transactional(readOnly = true)
    public List<Ord> findAllOrd() {
        return em.createQuery("select s from Ord s", Ord.class)
                .getResultList();
    }

    @Transactional
    public Ord updateOrd(Long id, Date CreateDate, Integer Count) {
        if (!StringUtils.hasText(CreateDate.toString())) {
            throw new IllegalArgumentException("Ord is null or empty");
        }
        final Ord currentOrd = findOrd(id);
        currentOrd.setCreateDate(CreateDate);
        currentOrd.setCount(Count);
        return em.merge(currentOrd);
    }

    @Transactional
    public Ord deleteOrd(Long id) {
        final Ord currentOrd = findOrd(id);
        em.remove(currentOrd);
        return currentOrd;
    }

    @Transactional
    public void deleteAllOrd() {
        em.createQuery("delete from Ord").executeUpdate();
    }
}
