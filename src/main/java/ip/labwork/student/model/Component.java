package ip.labwork.student.model;

import ip.labwork.student.service.ProductService;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column()
    private String ComponentName;
    private Integer Count;
    @ManyToMany(mappedBy = "components", fetch = FetchType.EAGER)
    private List<Product> products;


    public Component() {
    }

    public Component(String ComponentName, Integer Count) {
        this.ComponentName = ComponentName;
        this.Count = Count;
    }

    public Long getId() {
        return id;
    }

    public String getComponentName() {
        return ComponentName;
    }

    public void setComponentName(String ComponentName) {
        this.ComponentName = ComponentName;
    }
    public List<Product> getProduct() {
        /*if(products.contains(product)){
            return true;
        }else{
            return false;
        }*/
        return products;
    }
    public void setProduct(Product product) {
        if (products == null){
            products = new ArrayList<>();
        }
        this.products.add(product);
        if (!product.getComponents().contains(this)) { // warning this may cause performance issues if you have a large data set since this operation is O(n)
            product.getComponents().add(this);
        }
    }
    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer Count) {
        this.Count = Count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(id, component.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", ComponentName='" + ComponentName + '\'' +
                ", Count='" + Count + '\'' +
                '}';
    }
}
