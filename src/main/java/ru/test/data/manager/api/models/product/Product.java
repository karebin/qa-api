package ru.test.data.manager.api.models.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Product {
    private long clientId;
    private long id;
    private ProductType productType;
    private long balance;
}
