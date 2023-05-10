package ip.labwork.shop.controller;

import ip.labwork.shop.service.ComponentService;
import ip.labwork.user.model.UserRole;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/component")
public class ComponentMvcController {
    private final ComponentService componentService;

    public ComponentMvcController(ComponentService componentService) {
        this.componentService = componentService;
    }
    @GetMapping
    @Secured({UserRole.AsString.ADMIN})
    public String getComponents(Model model) {
        model.addAttribute("components", componentService.findAllComponent());
        return "component";
    }
    @GetMapping(value = {"/edit", "/edit/{id}"})
    @Secured({UserRole.AsString.ADMIN})
    public String editComponent(@PathVariable(required = false) Long id,
                                Model model) {
        if (id == null || id <= 0) {
            model.addAttribute("componentDto", new ComponentDTO());
        } else {
            model.addAttribute("componentId", id);
            model.addAttribute("componentDto", new ComponentDTO(componentService.findComponent(id)));
        }
        return "component-edit";
    }
    @PostMapping(value = {"/", "/{id}"})
    public String saveComponent(@PathVariable(required = false) Long id,
                                @ModelAttribute @Valid ComponentDTO componentDTO,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "component-edit";
        }
        if (id == null || id <= 0) {
            componentService.create(componentDTO);
        } else {
            componentService.updateComponent(id, componentDTO);
        }
        return "redirect:/component";
    }
    @PostMapping("/delete/{id}")
    public String deleteComponent(@PathVariable Long id) {
        componentService.deleteComponent(id);
        return "redirect:/component";
    }
}