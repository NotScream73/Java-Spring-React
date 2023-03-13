package ip.labwork.student.service;

import ip.labwork.student.model.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ComponentService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Component addComponent(String ComponentName, Integer Count) {
        if (!StringUtils.hasText(ComponentName) || Count == 0) {
            throw new IllegalArgumentException("Component is null or empty");
        }
        final Component component = new Component(ComponentName, Count);
        em.persist(component);
        return component;
    }

    @Transactional(readOnly = true)
    public Component findComponent(Long id) {
        final Component component = em.find(Component.class, id);
        if (component == null) {
            throw new EntityNotFoundException(String.format("Component with id [%s] is not found", id));
        }
        return component;
    }

    @Transactional(readOnly = true)
    public List<Component> findAllComponent() {
        return em.createQuery("select s from Component s", Component.class)
                .getResultList();
    }

    @Transactional
    public Component updateComponent(Long id, String ComponentName, Integer Count) {
        if (!StringUtils.hasText(ComponentName) || Count != 0) {
            throw new IllegalArgumentException("Component is null or empty");
        }
        final Component currentComponent = findComponent(id);
        currentComponent.setComponentName(ComponentName);
        currentComponent.setCount(Count);
        return em.merge(currentComponent);
    }

    @Transactional
    public Component deleteComponent(Long id) {
        final Component currentComponent = findComponent(id);
        em.remove(currentComponent);
        return currentComponent;
    }

    @Transactional
    public void deleteAllComponent() {
        em.createQuery("delete from Component").executeUpdate();
    }
}
