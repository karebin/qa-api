package ru.test.data.manager.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "public", name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "clientid", nullable = false)
    private long clientId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column(name = "balance", nullable = false)
    private long balance;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return clientId == that.clientId && balance == that.balance && Objects.equals(type, that.type) && Objects.equals(productType, that.productType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, type, productType, balance);
    }
}
