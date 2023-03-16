package ip.labwork.student.controller;

import ip.labwork.student.model.Component;
import ip.labwork.student.service.ComponentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/component")

public class ComponentController {
    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping("/add")
    public Component create(@RequestParam("name") String name,
                            @RequestParam("price") Integer price){
        return componentService.addComponent(name, price);
    }
    @GetMapping("/update")
    public Component update(@RequestParam("id") Long id,
                            @RequestParam("name") String name,
                            @RequestParam("price") Integer price){
        return componentService.updateComponent(id, name, price);
    }
    @GetMapping("/remove")
    public Component remove(@RequestParam("id") Long id){
        return componentService.deleteComponent(id);
    }
    @GetMapping("/removeAll")
    public void remove(){
        componentService.deleteAllComponent();
    }
    @GetMapping("/find")
    public Component find(@RequestParam("id") Long id){
        return componentService.findComponent(id);
    }
    @GetMapping("/findAll")
    public List<Component> findAll(){
        return componentService.findAllComponent();
    }
}
