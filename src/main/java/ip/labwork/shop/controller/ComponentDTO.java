package ip.labwork.shop.controller;

import ip.labwork.shop.model.Component;

public class ComponentDTO {
    private final long id;
    private final String componentName;
    private final int price;

    public ComponentDTO(Component component) {
        this.id = component.getId();
        this.componentName = component.getComponentName();
        this.price = component.getPrice();
    }

    public long getId() {
        return id;
    }

    public String getComponentName() {
        return componentName;
    }

    public int getPrice() {
        return price;
    }
}