package ip.labwork.shop.controller;

import ip.labwork.shop.service.ComponentService;
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
    public ComponentDTO createComponent(@RequestParam("name") String name,
                            @RequestParam("price") Integer price) {
        return new ComponentDTO(componentService.addComponent(name, price));
    }

    @PutMapping("/{id}")
    public ComponentDTO updateComponent(@PathVariable Long id,
                            @RequestParam("name") String name,
                            @RequestParam("price") Integer price) {
        return new ComponentDTO(componentService.updateComponent(id, name, price));
    }

    @DeleteMapping("/{id}")
    public ComponentDTO removeComponent(@PathVariable Long id) {
        return new ComponentDTO(componentService.deleteComponent(id));
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
        return componentService.findAllComponent().stream()
                .map(ComponentDTO::new)
                .toList();
    }
}