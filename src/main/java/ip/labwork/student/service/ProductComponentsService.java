package ip.labwork.student.service;

import ip.labwork.student.model.Component;
import ip.labwork.student.model.Product;
import ip.labwork.student.model.ProductComponents;
import ip.labwork.student.model.ProductComponentsKey;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class ProductComponentsService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public ProductComponents addProductComponents(Product product, Component component, Integer count) {
        final ProductComponents productComponents = new ProductComponents(component, product, count);
        product.addComponent(productComponents);
        component.addProduct(productComponents);
        em.persist(productComponents);
        return productComponents;
    }

   /* @Transactional(readOnly = true)
    public Component findProductComponent(Long productId, Long componentId) {
        em.find(Product.class, id);
        List<ProductComponents> productComponentsList = em.createQuery("select pc from ProductComponents pc", ProductComponents.class)
                .getResultList();
        final ProductComponentsKey productComponentsKey = new ProductComponentsKey(id,)
        final ProductComponents productComponents = em.find(ProductComponents.class, id);
        if (component == null) {
            throw new EntityNotFoundException(String.format("Component with id [%s] is not found", id));
        }
        return component;
    }*/

    @Transactional(readOnly = true)
    public List<ProductComponents> findAllProductComponents(Long id) {
        return em.createQuery("select p from ProductComponents p where id.productId = " + id, ProductComponents.class)
                .getResultList();
    }

   /* @Transactional
    public ProductComponents updateProduct(Product product, Component component, Integer Count) {

        //final Component currentComponent = fin(id);
        currentComponent.setComponentName(ProductName);
        currentComponent.setPrice(Count);
        return em.merge(currentComponent);
    }*/
    @Transactional
    public void deleteProduct(Product product) {
        int size = product.getComponents().size();
        for (int i = 0; i < size; i++){
            ProductComponents temp = product.getComponents().get(0);
            temp.getComponent().removeProduct(temp);
            temp.getProduct().removeComponent(temp);
            em.remove(temp);
        }
    }
    @Transactional
    public void deleteAllProduct() {
        em.createQuery("delete from ProductComponents").executeUpdate();
    }
    @Transactional
    public void removeAll(Long id, Long[] compid) {
        /*em.createQuery("delete from ProductComponents p where p.id.productId = " + id + " and p.id.componentId not in "+ compid).executeUpdate();
        Product product = em.find(Product.class, id);
        product.getComponents().clear();
        int s = 5;*/
        List<ProductComponents> temp = findAllProductComponents(id);
        for(int i = 0; i < temp.size(); i++){
            em.remove(temp.get(i));
        }
    }
}
