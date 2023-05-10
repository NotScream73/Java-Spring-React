package ip.labwork.test.controller;

import ip.labwork.configuration.WebConfiguration;
import ip.labwork.test.model.TestDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WebConfiguration.REST_API + "/test")
public class TestController {
    @PostMapping
    public TestDto testValidation(@RequestBody @Valid TestDto testDto) {
        return testDto;
    }
}
