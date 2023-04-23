package ip.labwork.shop.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import ip.labwork.shop.model.Component;
import jakarta.validation.constraints.NotBlank;

public class ComponentDTO {
    private long id;
    private String componentName;
    private int price;
    private int count = 0;
    public ComponentDTO(Component component) {
        this.id = component.getId();
        this.componentName = component.getComponentName();
        this.price = component.getPrice();
    }
    public ComponentDTO(Component component, int count) {
        this.id = component.getId();
        this.componentName = component.getComponentName();
        this.price = component.getPrice();
        this.count = count;
    }

    public ComponentDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}