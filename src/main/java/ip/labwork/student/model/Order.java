package ip.labwork.student.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name="tab_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date CreateDate;
    private Integer Count;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ords_product",
            joinColumns = @JoinColumn(name = "ord_fk"),
            inverseJoinColumns = @JoinColumn(name = "product_fk"))
    private List<Product> products;
    public Order() {
    }

    public Order(Date CreateDate, Integer Count) {
        this.CreateDate = CreateDate;
        this.Count = Count;
    }
    public void addProduct(Product product) {
        if (products == null){
            products = new ArrayList<>();
        }
        this.products.add(product);
        if (product.getOrder() == null) {
            product.setOrder(this);
        }
    }
    public Long getId() {
        return id;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date CreateDate) {
        this.CreateDate = CreateDate;
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
        Order ord = (Order) o;
        return Objects.equals(id, ord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ord{" +
                "id=" + id +
                ", CreateDate='" + CreateDate + '\'' +
                ", Count='" + Count + '\'' +
                '}';
    }

    public List<Product> getProducts() {
        return products;
    }
}
