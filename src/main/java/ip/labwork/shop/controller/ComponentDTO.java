package ip.labwork.shop.controller;

import ip.labwork.shop.model.Component;

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

    public String getComponentName() {
        return componentName;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }
}