package ru.test.data.manager.api.models.product;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductList {
    private long clientId;
    private List<Product> products;
}
