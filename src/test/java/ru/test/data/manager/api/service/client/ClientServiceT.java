package ru.test.data.manager.api.service.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import ru.test.data.manager.api.entity.ClientEntity;
import ru.test.data.manager.api.entity.ProductEntity;
import ru.test.data.manager.api.models.client.Client;
import ru.test.data.manager.api.models.client.ClientWithOutProducts;
import ru.test.data.manager.api.models.client.ContactInfo;
import ru.test.data.manager.api.models.product.Product;
import ru.test.data.manager.api.models.product.ProductType;
import ru.test.data.manager.api.repository.client.ClientRepository;
import ru.test.data.manager.api.repository.product.ProductRepository;
import ru.test.data.manager.api.services.client.ClientService;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = ClientServiceT.class)
@ContextConfiguration(initializers = {TestContainerPostgres.class})
@Testcontainers
@ComponentScan({"ru.test.data.manager.api"})
@Execution(ExecutionMode.CONCURRENT)
@Transactional
public class ClientServiceT {
    Logger log = LogManager.getLogger("");
    @Autowired
    ClientService clientService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ClientRepository clientRepository;

    ClientWithOutProducts testClient = new ClientWithOutProducts(
            999,
            "ИльяТ",
            "КаребинТ",
            new ContactInfo("79999999999", "test@mail.com"));

    String clientWithProductPhoneNumber = "79991232293";
    String clientWithOutProductPhoneNumber = "79991232233";

    private Product productEntityToProduct(ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .clientId(productEntity.getClientId())
                .productType(new ProductType(productEntity.getType(), productEntity.getProductType()))
                .balance(productEntity.getBalance())
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

    @DisplayName("Получение случайного клиента")
    @Test
    public void checkGetRandomClient() {
        assertThat("Клиент не получен", clientService.getRandomClient().getId() != 0);
    }

    @DisplayName("Проверка клиентских данных из БД и возвращаемых сервисом")
    @Test
    public void checkGetClientDataWithOutProduct() {
        Client clientFromService = clientService.getClientByPhone(clientWithProductPhoneNumber);
        ClientEntity clientEntity = clientRepository.findByMobilePhone(clientWithProductPhoneNumber);
        Client clientFromEntity = Client.builder()
                .id(clientEntity.getId())
                .firstName(clientEntity.getFirstName())
                .lastName(clientEntity.getLastName())
                .contactInfo(new ContactInfo(clientEntity.getMobilePhone(), clientEntity.getEmail()))
                .products(new ArrayList<>())
                .build();
        assertEquals("Данные клиента из бд и сервиса не идентичны", clientFromService, clientFromEntity);
    }

    @DisplayName("Проверка продуктов возвращаемых с клиентом")
    @Test
    public void checkGetClientProductDataByServiceAndEntity() {
        Client clientFromService = clientService.getClientByPhone(clientWithOutProductPhoneNumber);
        List<ProductEntity> clientProductEntities = productRepository.findAllByClientId(clientFromService.getId());

        Client productFromEntity = Client.builder()
                .firstName(clientFromService.getFirstName())
                .contactInfo(new ContactInfo(clientFromService.getContactInfo().getMobilePhone(), null))
                .products(productEntityListToProductList(clientProductEntities))
                .build();
        assertEquals("Продукты клиента из сервиса, не соответстуют продуктам из БД",
                clientFromService.getProducts(), productFromEntity.getProducts());
    }

    @DisplayName("Получение клиента по номеру телефона (без проверки продуктов)")
    @Test
    public void checkClientByPhoneNumberByServiceAndEntity() {
        String testClientMobilePhone = "79991232233";
        Client clientFromService = clientService.getClientByPhone(testClientMobilePhone);
        ClientEntity clientEntity = clientRepository.findByMobilePhone(testClientMobilePhone);

        Client clientFromEntity = Client.builder()
                .id(clientEntity.getId())
                .contactInfo(new ContactInfo(clientEntity.getMobilePhone(), clientEntity.getEmail()))
                .firstName(clientEntity.getFirstName())
                .lastName(clientEntity.getLastName())
                .build();

        assertEquals("Номер телефона найденного клиента не соответствует искомому",
                clientFromService,
                clientFromEntity);
    }

    @DisplayName("Успешное добавление клиента")
    @Transactional
    @Test
    public void checkSuccessClientAdd() {
        clientService.addClient(testClient);
        Client clientReceivedByService = clientService.getClientByPhone(testClient.getContactInfo().getMobilePhone());
        assertEquals("Номер телефона найденного клиента не соответствует искомому",
                testClient.getContactInfo().getMobilePhone(),
                clientReceivedByService.getContactInfo().getMobilePhone());
    }
}



