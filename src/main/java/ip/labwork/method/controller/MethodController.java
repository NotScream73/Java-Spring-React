package ip.labwork.method.controller;

import ip.labwork.configuration.WebConfiguration;
import ip.labwork.method.service.MethodService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WebConfiguration.REST_API + "/method")
public class MethodController {
    private final MethodService speakerService;

    public MethodController(MethodService speakerService) {
        this.speakerService = speakerService;
    }

    @GetMapping("/sum")
    public String Sum(@RequestParam(value = "first", defaultValue = "1") Object first,
                      @RequestParam(value = "second", defaultValue = "1") Object second,
                      @RequestParam(value = "type", defaultValue = "int") String type) {
        return speakerService.Sum(first,second,type);
    }

    @GetMapping("/minus")
    public String Ras(@RequestParam(value = "first", defaultValue = "1") Object first,
                      @RequestParam(value = "second", defaultValue = "1") Object second,
                      @RequestParam(value = "type", defaultValue = "int") String type) {
        return speakerService.Ras(first,second,type);
    }

    @GetMapping("/multi")
    public String Pros(@RequestParam(value = "first", defaultValue = "1") Object first,
                       @RequestParam(value = "second", defaultValue = "1") Object second,
                       @RequestParam(value = "type", defaultValue = "int") String type) {
        return speakerService.Pros(first,second,type);
    }

    @GetMapping("/div")
    public String Del(@RequestParam(value = "first", defaultValue = "1") Object first,
                      @RequestParam(value = "second", defaultValue = "1") Object second,
                      @RequestParam(value = "type", defaultValue = "int") String type) {
        return speakerService.Del(first,second,type);
    }
}
