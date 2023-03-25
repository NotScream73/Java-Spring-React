package ip.labwork.shop.service;

import ip.labwork.shop.model.Component;
import ip.labwork.shop.model.ProductComponents;
import ip.labwork.shop.repository.ComponentRepository;
import ip.labwork.shop.repository.ProductComponentRepository;
import ip.labwork.util.validation.ValidatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final ProductComponentRepository productComponentRepository;
    private final ValidatorUtil validatorUtil;

    public ComponentService(ComponentRepository componentRepository,
                          ValidatorUtil validatorUtil, ProductComponentRepository productComponentRepository) {
        this.componentRepository = componentRepository;
        this.validatorUtil = validatorUtil;
        this.productComponentRepository = productComponentRepository;
    }

    @Transactional
    public Component addComponent(String componentName, Integer price) {
        final Component component = new Component(componentName, price);
        validatorUtil.validate(component);
        return componentRepository.save(component);
    }

    @Transactional(readOnly = true)
    public Component findComponent(Long id) {
        final Optional<Component> student = componentRepository.findById(id);
        return student.orElseThrow(() -> new ComponentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Component> findAllComponent() {
        return componentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Component> findFiltredComponents(Long[] arr) {
        return componentRepository.findAllById(Arrays.stream(arr).toList());
    }

    @Transactional
    public Component updateComponent(Long id, String componentName, Integer price) {
        final Component currentComponent = findComponent(id);
        currentComponent.setComponentName(componentName);
        currentComponent.setPrice(price);
        validatorUtil.validate(currentComponent);
        return componentRepository.save(currentComponent);
    }

    @Transactional
    public Component deleteComponent(Long id) {
        final Component currentComponent = findComponent(id);
        int size = currentComponent.getProducts().size();
        for (int i = 0; i < size; i++) {
            ProductComponents temp = currentComponent.getProducts().get(0);
            temp.getComponent().removeProduct(temp);
            temp.getProduct().removeComponent(temp);
            productComponentRepository.delete(temp);
        }
        componentRepository.delete(currentComponent);
        return currentComponent;
    }
    @Transactional
    public void deleteAllComponent() {
        productComponentRepository.deleteAll();
        componentRepository.deleteAll();
    }
}
