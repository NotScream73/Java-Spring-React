package ip.labwork.shop.service;

import ip.labwork.shop.controller.OrderDTO;
import ip.labwork.shop.model.Order;
import ip.labwork.shop.model.OrderProducts;
import ip.labwork.shop.model.Product;
import ip.labwork.shop.repository.OrderRepository;
import ip.labwork.shop.repository.ProductRepository;
import ip.labwork.user.service.UserService;
import ip.labwork.util.validation.ValidatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ValidatorUtil validatorUtil;

    public OrderService(OrderRepository orderRepository,
                        ValidatorUtil validatorUtil, ProductRepository productRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.validatorUtil = validatorUtil;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        int price = 0;
        for (int i = 0; i < orderDTO.getProductDTOList().size(); i++) {
            price += orderDTO.getProductDTOList().get(i).getPrice() * orderDTO.getProductDTOList().get(i).getCount();
        }
        final Order order = new Order(new Date(), price, orderDTO.getStatus(), userService.findByLogin(orderDTO.getUser()).getId());
        validatorUtil.validate(order);
        orderRepository.save(order);
        for (int i = 0; i < orderDTO.getProductDTOList().size(); i++) {
            final OrderProducts orderProducts = new OrderProducts(order, productRepository.findById(orderDTO.getProductDTOList().get(i).getId()).orElseThrow(() -> new ProductNotFoundException(1L)), orderDTO.getProductDTOList().get(i).getCount());
            order.addProduct(orderProducts);
        }
        orderRepository.save(order);
        return new OrderDTO(findOrder(order.getId()));
    }

    @Transactional(readOnly = true)
    public Order findOrder(Long id) {
        final Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> findAllOrder() {
        return orderRepository.findAll().stream().map(x -> new OrderDTO(x)).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> findFiltredOrder(String login) {
        return orderRepository.findAll().stream().filter(x -> Objects.equals(x.getUser_id(), userService.findByLogin(login).getId())).map(x -> new OrderDTO(x)).toList();
    }

    @Transactional
    public OrderDTO update(Long id, OrderDTO orderDTO) {
        final Order currentOrder = findOrder(id);
        currentOrder.setDate(orderDTO.getDate());
        currentOrder.setPrice(orderDTO.getPrice());
        validatorUtil.validate(currentOrder);
        orderRepository.save(currentOrder);
        List<OrderProducts> orderProductsList = orderRepository.getOrderProduct(id);
        List<Long> product_id = new ArrayList<>(orderProductsList.stream().map(p -> p.getId().getProductId()).toList());
        List<Product> newProducts = productRepository.findAllById(orderDTO.getProductDTOList().stream().map(x -> x.getId()).toList());
        for (int i = 0; i < newProducts.size(); i++) {
            final Long currentId = newProducts.get(i).getId();
            if (product_id.contains(currentId)) {
                final OrderProducts orderProducts = orderProductsList.stream().filter(x -> x.getId().getProductId().equals(currentId)).findFirst().get();
                orderProductsList.remove(orderProducts);
                currentOrder.removeProducts(orderProducts);
                currentOrder.addProduct(new OrderProducts(currentOrder, newProducts.get(i), orderDTO.getProductDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount()));
                int finalI = i;
                product_id = product_id.stream().filter(x -> !Objects.equals(x, newProducts.get(finalI).getId())).toList();
                orderProducts.setCount(orderDTO.getProductDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount());
                orderRepository.saveAndFlush(currentOrder);
            } else {
                final OrderProducts orderProducts = new OrderProducts(currentOrder, newProducts.get(i), orderDTO.getProductDTOList().stream().filter(x -> x.getId() == currentId).toList().get(0).getCount());
                currentOrder.addProduct(orderProducts);
                orderRepository.saveAndFlush(currentOrder);
            }
        }
        for (int i = 0; i < orderProductsList.size(); i++) {
            orderProductsList.get(i).getProduct().removeOrder(orderProductsList.get(i));
            orderProductsList.get(i).getOrder().removeProducts(orderProductsList.get(i));
        }
        orderRepository.saveAndFlush(currentOrder);
        return new OrderDTO(currentOrder);
    }

    @Transactional
    public OrderDTO deleteOrder(Long id) {
        final Order currentOrder = findOrder(id);
        int size = currentOrder.getProducts().size();
        for (int i = 0; i < size; i++) {
            OrderProducts temp = currentOrder.getProducts().get(0);
            temp.getProduct().removeOrder(temp);
            temp.getOrder().removeProducts(temp);
        }
        orderRepository.delete(currentOrder);
        return new OrderDTO(currentOrder);
    }

    @Transactional
    public void deleteAllOrder() {
        orderRepository.deleteAll();
    }
}