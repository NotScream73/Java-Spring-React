package ip.labwork.shop.controller;

import ip.labwork.shop.service.ComponentService;
import ip.labwork.user.model.UserRole;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/component")

public class ComponentController {
    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @PostMapping
    @Secured({UserRole.AsString.ADMIN})
    public ComponentDTO createComponent(@RequestBody @Valid ComponentDTO componentDTO) {
        return componentService.create(componentDTO);
    }

    @PutMapping("/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public ComponentDTO updateComponent(@PathVariable Long id, @RequestBody @Valid ComponentDTO componentDTO) {
        return componentService.updateComponent(id, componentDTO);
    }

    @DeleteMapping("/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public ComponentDTO removeComponent(@PathVariable Long id) {
        return componentService.deleteComponent(id);
    }

    @DeleteMapping
    @Secured({UserRole.AsString.ADMIN})
    public void removeAllComponent() {
        componentService.deleteAllComponent();
    }

    @GetMapping("/{id}")
    @Secured({UserRole.AsString.ADMIN})
    public ComponentDTO findComponent(@PathVariable Long id) {
        return new ComponentDTO(componentService.findComponent(id));
    }

    @GetMapping
    @Secured({UserRole.AsString.ADMIN})
    public List<ComponentDTO> findAllComponent() {
        return componentService.findAllComponent();
    }
}