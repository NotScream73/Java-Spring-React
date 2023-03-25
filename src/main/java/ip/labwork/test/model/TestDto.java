package ip.labwork.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TestDto {
    @NotNull(message = "Id can't be null")
    private Long id;
    @NotBlank(message = "Name can't be null or empty")
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getData() {
        return String.format("%s %s", id, name);
    }

    @JsonIgnore
    public String getAnotherData() {
        return "Test";
    }
}