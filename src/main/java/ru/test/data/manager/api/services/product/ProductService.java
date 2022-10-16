package ru.test.data.manager.api.services.product;

import org.springframework.stereotype.Service;
import ru.test.data.manager.api.models.product.Product;
import ru.test.data.manager.api.models.product.ProductList;

import java.util.List;

@Service
public interface ProductService {

    List<Product> getProductList();

    List<Product> getClientProductList(int clientId);

    ProductList getProductListByClient(long clientId);

    Product addProductByClientId(Product product, long clientId);
}
