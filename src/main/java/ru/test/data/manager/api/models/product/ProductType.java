package ru.test.data.manager.api.models.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class ProductType {
    private String type;
    private String productType;
}
