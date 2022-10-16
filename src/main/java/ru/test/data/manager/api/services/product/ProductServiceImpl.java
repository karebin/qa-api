package ru.test.data.manager.api.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.test.data.manager.api.entity.ProductEntity;
import ru.test.data.manager.api.exception.erroe.client.ClientNotFound;
import ru.test.data.manager.api.exception.erroe.product.ProductValidation;
import ru.test.data.manager.api.models.product.Product;
import ru.test.data.manager.api.models.product.ProductList;
import ru.test.data.manager.api.models.product.ProductType;
import ru.test.data.manager.api.repository.product.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductList() {
        return productEntityListToProductList(productRepository.findAll(Sort.by("clientId")));
    }

    public List<Product> getClientProductList(int clientId) {
        return productEntityListToProductList(productRepository.findAllByClientId(clientId));
    }

    public ProductList getProductListByClient(long clientId) {
        return
                new ProductList().builder()
                        .clientId(clientId)
                        .products(productEntityListToProductList(productRepository.findAllByClientId(clientId)))
                        .build();

    }

    public Product addProductByClientId(Product product, long clientId) {
        if (product.getProductType() == null
                || product.getProductType().getType() == null
                || product.getProductType().getProductType() == null)
            throw new ProductValidation("Fields: Type or ProductType is empty");

        if (product.getBalance() < 0 || product.getBalance() > 4000000)
            throw new ProductValidation("Balance cannot be negative or more that 4 000 000");

        try {
            return productEntityToProduct(productRepository.save(productToProductEntityWithClientIdParam(product, clientId)));
        } catch (RuntimeException e) {
            throw new ClientNotFound("Client for add product not found");
        }
    }

    private Product productEntityToProduct(ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .clientId(productEntity.getClientId())
                .productType(new ProductType(productEntity.getType(), productEntity.getProductType()))
                .balance(productEntity.getBalance())
                .build();
    }

    private ProductEntity productToProductEntity(Product product) {
        return ProductEntity.builder()
                .clientId(product.getClientId())
                .productType(product.getProductType().getProductType())
                .type(product.getProductType().getType())
                .balance(product.getBalance())
                .build();
    }

    private ProductEntity productToProductEntityWithClientIdParam(Product product, long clientId) {
        return ProductEntity.builder()
                .clientId(clientId)
                .productType(product.getProductType().getProductType())
                .type(product.getProductType().getType())
                .balance(product.getBalance())
                .build();
    }


    private List<Product> productEntityListToProductList(List<ProductEntity> productEntities) {
        List<Product> productList = new ArrayList<>();
        productEntities.forEach(pe -> {
                    productList.add(productEntityToProduct(pe));
                }
        );
        return productList;
    }
}
