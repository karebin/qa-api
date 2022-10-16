package ru.test.data.manager.api.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(classes = ProductServiceT.class)
@ContextConfiguration(initializers = {TestContainerPostgres.class})
@Testcontainers
@ComponentScan({"ru.test.data.manager.api"})
@ActiveProfiles("test")
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


    @DisplayName("Добавление продукта")
    @Test
    public void checkAddProductToClient() {
        productService.addProductByClientId(testProductObj, testClient.getId());
        List<ProductEntity> productEntity = productRepository.findAllByClientId(testClient.getId());
        long productId = productEntity.stream().findFirst()
                .filter(productEntity1 -> productEntity1.getBalance() == 30001)
                .get().getId();

        testProductObj.setId(productId);
        assertThat("Не найден добавленный продукт",
                clientService.getClientByPhone(testClient.getContactInfo().getMobilePhone())
                        .getProducts().stream()
                        .anyMatch(product -> product.equals(testProductObj)));
    }
}