package ip.labwork.shop.service;

import ip.labwork.shop.model.Component;
import ip.labwork.shop.model.OrderProducts;
import ip.labwork.shop.model.Product;
import ip.labwork.shop.model.ProductComponents;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComponentService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Component addComponent(String componentName, Integer price) {
        if (!StringUtils.hasText(componentName) || price == 0) {
            throw new IllegalArgumentException("Component is null or empty");
        }
        final Component component = new Component(componentName, price);
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
        return em.createQuery("select c from Component c", Component.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Component> findFiltredComponents(Long[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array id is empty");
        }
        List<Component> componentList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            componentList.add(em.find(Component.class, arr[i]));
        }
        return componentList;
    }

    @Transactional
    public Component updateComponent(Long id, String componentName, Integer price) {
        if (!StringUtils.hasText(componentName) || price == 0) {
            throw new IllegalArgumentException("Component is null or empty");
        }
        final Component currentComponent = findComponent(id);
        currentComponent.setComponentName(componentName);
        currentComponent.setPrice(price);
        return em.merge(currentComponent);
    }

    @Transactional
    public Component deleteComponent(Long id) {
        final Component currentComponent = findComponent(id);
        int size = currentComponent.getProducts().size();
        for (int i = 0; i < size; i++) {
            ProductComponents temp = currentComponent.getProducts().get(0);
            temp.getComponent().removeProduct(temp);
            temp.getProduct().removeComponent(temp);
            em.remove(temp);
        }
        em.remove(currentComponent);
        return currentComponent;
    }
    @Transactional
    public void deleteAllComponent() {
        em.createQuery("delete from ProductComponents").executeUpdate();
        em.createQuery("delete from Component").executeUpdate();
    }
}
