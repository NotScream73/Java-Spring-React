package ip.labwork.student.service;

import ip.labwork.student.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderProductsService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public OrderProducts addOrderProducts(Order order, Product product, Integer count) {
        final OrderProducts orderProducts = new OrderProducts(order, product, count);
        order.addProduct(orderProducts);
        product.addOrder(orderProducts);
        em.persist(orderProducts);
        return orderProducts;
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
    public List<OrderProducts> findAllOrderProducts(Long id) {
        return em.createQuery("select o from OrderProducts o where id.orderId = " + id, OrderProducts.class)
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
    public void deleteOrder(Order order) {
        int size = order.getProducts().size();
        for (int i = 0; i < size; i++){
            OrderProducts temp = order.getProducts().get(0);
            temp.getProduct().removeOrder(temp);
            temp.getProduct().removeOrder(temp);
            em.remove(temp);
        }
    }
    @Transactional
    public void deleteAllOrder() {
        em.createQuery("delete from OrderProducts").executeUpdate();
    }
    @Transactional
    public void removeAll(Long id, Long[] prodid) {
        /*em.createQuery("delete from ProductComponents p where p.id.productId = " + id + " and p.id.componentId not in "+ compid).executeUpdate();
        Product product = em.find(Product.class, id);
        product.getComponents().clear();
        int s = 5;*/
        List<OrderProducts> temp = findAllOrderProducts(id);
        for(int i = 0; i < temp.size(); i++){
            em.remove(temp.get(i));
        }
    }

    public void update(Order order, Product product, Integer count, Long[] prod) {
        for (int i = 0; i < prod.length; i++){
            List<OrderProducts> tem = em.createQuery("select o from OrderProducts o where o.id.orderId = " + order.getId() + " and o.id.productId = " + product.getId(), OrderProducts.class)
                    .getResultList();
            if (tem.size() != 0){
                final OrderProducts orderProducts = tem.get(0);
                orderProducts.setCount(count);
                em.merge(orderProducts);
            }else{
                final OrderProducts orderProducts = new OrderProducts(order, product, count);
                order.addProduct(orderProducts);
                product.addOrder(orderProducts);
                em.persist(orderProducts);
            }
        }
        List<OrderProducts> newList = em.createQuery("select o from OrderProducts o where o.id.orderId = " + order.getId(), OrderProducts.class).getResultList();

        for(int i =0; i < newList.size(); i++){
            boolean flag = false;
            for (int j = 0; j < prod.length; j++){

                if (Objects.equals(newList.get(i).getId().getProductId(), prod[j])) {
                    flag = true;
                    break;
                }
            }
            if (!flag){
                newList.get(i).getProduct().removeOrder(newList.get(i));
                newList.get(i).getOrder().removeProducts(newList.get(i));
                em.remove(newList.get(i));
            }
        }
    }
}
