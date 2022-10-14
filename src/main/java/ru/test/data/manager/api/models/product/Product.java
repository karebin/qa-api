package ru.test.data.manager.api.models.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.test.data.manager.api.models.productEnum.ProductType;


@Data
@Builder
@AllArgsConstructor
public class Product {
    private long clientId;
    private long id;
    @NonNull
    private ProductType productType;
    @NonNull
    private long balance;
}
