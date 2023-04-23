package ip.labwork.shop.controller;

import ip.labwork.WebConfiguration;
import ip.labwork.shop.service.ComponentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(WebConfiguration.REST_API + "/component")

public class ComponentController {
    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @PostMapping
    public ComponentDTO createComponent(@RequestBody @Valid ComponentDTO componentDTO) {
        return componentService.create(componentDTO);
    }

    @PutMapping("/{id}")
    public ComponentDTO updateComponent(@PathVariable Long id,@RequestBody @Valid ComponentDTO componentDTO) {
        return componentService.updateComponent(id,componentDTO);
    }

    @DeleteMapping("/{id}")
    public ComponentDTO removeComponent(@PathVariable Long id) {
        return componentService.deleteComponent(id);
    }

    @DeleteMapping
    public void removeAllComponent() {
        componentService.deleteAllComponent();
    }

    @GetMapping("/{id}")
    public ComponentDTO findComponent(@PathVariable Long id) {
        return new ComponentDTO(componentService.findComponent(id));
    }

    @GetMapping
    public List<ComponentDTO> findAllComponent() {
        return componentService.findAllComponent();
    }
}