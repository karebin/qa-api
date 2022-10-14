package ru.test.data.manager.api.models.productEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class ProductType {
    @NonNull
    private String type;
    @NonNull
    private String productType;
}
