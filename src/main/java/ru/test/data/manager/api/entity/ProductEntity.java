package ru.test.data.manager.api.entity;

import lombok.*;

import javax.persistence.*;

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


}
