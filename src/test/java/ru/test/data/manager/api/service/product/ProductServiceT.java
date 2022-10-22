package ru.test.data.manager.api.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.test.data.manager.api.db.TestContainerPostgres;
import ru.test.data.manager.api.entity.ProductEntity;
import ru.test.data.manager.api.models.client.Client;
import ru.test.data.manager.api.models.client.ContactInfo;
import ru.test.data.manager.api.models.product.Product;
import ru.test.data.manager.api.models.product.ProductType;
import ru.test.data.manager.api.repository.product.ProductRepository;
import ru.test.data.manager.api.services.client.ClientService;
import ru.test.data.manager.api.services.product.ProductService;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = ProductServiceT.class)
@ContextConfiguration(initializers = {TestContainerPostgres.class})
@Testcontainers
@ComponentScan({"ru.test.data.manager.api"})
@Execution(ExecutionMode.CONCURRENT)
@Transactional
public class ProductServiceT {
    @Autowired
    ClientService clientService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    Client testClient = Client.builder()
            .firstName("Илья")
            .contactInfo(new ContactInfo("79991232238", null))
            .id(8)
            .build();
    Product testProductObj = Product.builder()
            .clientId(testClient.getId())
            .balance(30001)
            .productType(new ProductType("LOAN", "Test_loan"))
            .build();

    ProductEntity testProductObjEntity = ProductEntity.builder()
            .clientId(testProductObj.getClientId())
            .balance(testProductObj.getBalance())
            .productType(testProductObj.getProductType().getProductType())
            .type(testProductObj.getProductType().getType())
            .build();

    @DisplayName("Проверка сохранения добавленого продукта в БД")
    @Test
    @Transactional
    public void checkAddProductToClientInDB() {
        long productId = Long.parseLong(String.valueOf((productService
                .addProductByClientId(testProductObj, testClient.getId())
                .getId())));
        ProductEntity productEntityFromDB = productRepository.findProductByClientIdAndId(testClient.getId(), productId);
        assertEquals("Объект сохраненный в БД, не соответствует отправленному", productEntityFromDB, testProductObjEntity);
    }
}