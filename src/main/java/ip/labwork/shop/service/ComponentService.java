package ip.labwork.shop.service;

import ip.labwork.shop.controller.ComponentDTO;
import ip.labwork.shop.model.Component;
import ip.labwork.shop.model.ProductComponents;
import ip.labwork.shop.repository.ComponentRepository;
import ip.labwork.util.validation.ValidatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final ValidatorUtil validatorUtil;

    public ComponentService(ComponentRepository componentRepository,
                          ValidatorUtil validatorUtil) {
        this.componentRepository = componentRepository;
        this.validatorUtil = validatorUtil;
    }

    @Transactional
    public ComponentDTO create(ComponentDTO componentDTO) {
        final Component component = new Component(componentDTO.getComponentName(), componentDTO.getPrice());
        validatorUtil.validate(component);
        return new ComponentDTO(componentRepository.save(component));
    }
    @Transactional(readOnly = true)
    public Component findComponent(Long id) {
        final Optional<Component> component = componentRepository.findById(id);
        return component.orElseThrow(() -> new ComponentNotFoundException(id));
    }
    @Transactional(readOnly = true)
    public List<ComponentDTO> findAllComponent() {
        return componentRepository.findAll().stream().map(x -> new ComponentDTO(x)).toList();
    }
    @Transactional
    public ComponentDTO updateComponent(Long id, ComponentDTO component) {
        final Component currentComponent = findComponent(id);
        currentComponent.setComponentName(component.getComponentName());
        currentComponent.setPrice(component.getPrice());
        validatorUtil.validate(currentComponent);
        return new ComponentDTO(componentRepository.save(currentComponent));
    }
    @Transactional
    public ComponentDTO deleteComponent(Long id) {
        final Component currentComponent = findComponent(id);
        int size = currentComponent.getProducts().size();
        for (int i = 0; i < size; i++) {
            ProductComponents productComponents = currentComponent.getProducts().get(0);
            productComponents.getComponent().removeProduct(productComponents);
            productComponents.getProduct().removeComponent(productComponents);
        }
        componentRepository.delete(currentComponent);
        return new ComponentDTO(currentComponent);
    }
    @Transactional
    public void deleteAllComponent() {
        componentRepository.deleteAll();
    }
}