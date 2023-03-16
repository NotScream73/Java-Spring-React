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

import java.util.ArrayList;
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

    @Transactional(readOnly = true)
    public List<ProductComponents> findAllProductComponents(Long id) {
        return em.createQuery("select p from ProductComponents p where id.productId = " + id, ProductComponents.class)
                .getResultList();
    }

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
        List<ProductComponents> temp = findAllProductComponents(id);
        for(int i = 0; i < temp.size(); i++){
            em.remove(temp.get(i));
        }
    }
    @Transactional
    public void update(Product product, Component component, Integer count, Long[] comp) {
        for (int i = 0; i < comp.length; i++){
            List<ProductComponents> tem = em.createQuery("select p from ProductComponents p where p.id.productId = " + product.getId() + " and p.id.componentId = " + component.getId(), ProductComponents.class)
                    .getResultList();
            if (tem.size() != 0){
                final ProductComponents productComponents = tem.get(0);
                productComponents.setCount(count);
                em.merge(productComponents);
            }else{
                final ProductComponents productComponents = new ProductComponents(component, product, count);
                product.addComponent(productComponents);
                component.addProduct(productComponents);
                em.persist(productComponents);
            }
        }
        List<ProductComponents> newList = em.createQuery("select p from ProductComponents p where p.id.productId = " + product.getId(), ProductComponents.class).getResultList();

        for(int i =0; i < newList.size(); i++){
            boolean flag = false;
            for (int j = 0; j < comp.length; j++){

                if (Objects.equals(newList.get(i).getId().getComponentId(), comp[j])) {
                    flag = true;
                    break;
                }
            }
            if (!flag){
                newList.get(i).getComponent().removeProduct(newList.get(i));
                newList.get(i).getProduct().removeComponent(newList.get(i));
                em.remove(newList.get(i));
            }
        }
    }
}
