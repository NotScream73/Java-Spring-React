package ip.labwork.shop.controller;

import ip.labwork.shop.model.Component;
import ip.labwork.shop.model.Product;
import ip.labwork.shop.model.ProductComponents;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductDTO {
    private final long id;
    private final String productName;
    private final int price;
    private int count = 0;
    private final List<ComponentDTO> componentDTOList;
    public ProductDTO(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.componentDTOList = product.getComponents().stream()
                                .filter(x -> Objects.equals(x.getId().getProductId(), product.getId()))
                                .map(y -> new ComponentDTO(y.getComponent(), y.getCount()))
                                .toList();
    }
    public ProductDTO(Product product, int count) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.componentDTOList = product.getComponents().stream()
                .filter(x -> Objects.equals(x.getId().getProductId(), product.getId()))
                .map(y -> new ComponentDTO(y.getComponent(), y.getCount()))
                .toList();
        this.count = count;
    }
    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public List<ComponentDTO> getComponentDTOList() {
        return componentDTOList;
    }


    public int getCount() {
        return count;
    }
}