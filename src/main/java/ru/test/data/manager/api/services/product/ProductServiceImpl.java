package ru.test.data.manager.api.services.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.test.data.manager.api.entity.ProductEntity;
import ru.test.data.manager.api.models.product.Product;
import ru.test.data.manager.api.models.product.ProductList;
import ru.test.data.manager.api.models.productEnum.ProductType;
import ru.test.data.manager.api.repository.product.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public List<Product> getProductList() {
        return productEntityListToProductList(productRepository.findAll(Sort.by("clientId")));
    }

    @Override
    public List<Product> getClientProductList(int clientId) {
        return productEntityListToProductList(productRepository.findAllByClientId(clientId));
    }

    @Override
    public ProductList getProductListByClient(long clientId) {
        return
                new ProductList().builder()
                        .clientId(clientId)
                        .products(productEntityListToProductList(productRepository.findAllByClientId(clientId)))
                        .build();

    }

    public Product addProductByClientId(Product product, long clientId) {
        return productEntityToProduct(productRepository.save(productToProductEntityWithClientIdParam(product,clientId)));
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
