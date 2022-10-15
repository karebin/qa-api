package ru.test.data.manager.api.service.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.test.data.manager.api.entity.ClientEntity;
import ru.test.data.manager.api.entity.ProductEntity;
import ru.test.data.manager.api.models.client.Client;
import ru.test.data.manager.api.models.client.ClientWithOutProducts;
import ru.test.data.manager.api.models.client.ContactInfo;
import ru.test.data.manager.api.models.product.Product;
import ru.test.data.manager.api.models.productEnum.ProductType;
import ru.test.data.manager.api.repository.client.ClientRepository;
import ru.test.data.manager.api.repository.product.ProductRepository;
import ru.test.data.manager.api.services.client.ClientService;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = ClientServiceT.class)
@ContextConfiguration(initializers = {ClientServiceT.Initializer.class})
@Testcontainers
@ComponentScan({"ru.test.data.manager.api"})
@ActiveProfiles("test")
public class ClientServiceT {

    Logger log = LogManager.getLogger("");

    @Autowired
    ClientService clientService;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ClientRepository clientRepository;

    @Container
    public static PostgreSQLContainer postgres = (PostgreSQLContainer) new PostgreSQLContainer("postgres:alpine")
            .withDatabaseName("postgresTest")
            .withUsername("postgresTest")
            .withPassword("postgresTest")
            .withInitScript("db.sql");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            System.out.println("УРЛ " + postgres.getJdbcUrl());
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext);
            System.out.println(configurableApplicationContext.getEnvironment());
        }
    }

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

    @DisplayName("Клиентские данные (без продуктов) возращаемые сервисом идентичны данным в БД")
    @Test
    public void checkGetClientDataWithOutProduct() {
        Client clientFromService = clientService.getClientByPhone(clientWithProductPhoneNumber);
        ClientEntity clientFromDB = clientRepository.findByMobilePhone(clientWithProductPhoneNumber);
        Client clientFromDB2 = Client.builder()
                .id(clientFromDB.getId())
                .firstName(clientFromDB.getFirstName())
                .lastName(clientFromDB.getLastName())
                .contactInfo(new ContactInfo(clientFromDB.getMobilePhone(), clientFromDB.getEmail()))
                .products(new ArrayList<>())
                .build();
        assertEquals("Данные клиента из бд и сервиса не идентичны", clientFromService, clientFromDB2);
    }

    @DisplayName("Клиентские данные (продукты) возращаемые сервисом идентичны данным в БД")
    @Test
    public void checkGetClientDataWithProduct() {
        Client clientFromService = clientService.getClientByPhone(clientWithOutProductPhoneNumber);
        List<ProductEntity> clientProductsFromDb = productRepository.findAllByClientId(clientFromService.getId());

        Client buildClientWithProductFromDB = Client.builder()
                .firstName(clientFromService.getFirstName())
                .contactInfo(new ContactInfo(clientFromService.getContactInfo().getMobilePhone(), null))
                .products(productEntityListToProductList(clientProductsFromDb))
                .build();
        assertEquals("Продукты клиента из сервиса, не соответстуют продуктам из БД",
                clientFromService.getProducts(), buildClientWithProductFromDB.getProducts());
    }

    @DisplayName("Получение всех клиентов из БД")
    @Test
    public void checkGetAllClientFromDb() {
        int allClientFromService = clientService.getAllClient().size();
        int allClientFromDb = clientRepository.findAll().size();
        assertEquals("Кол-во клиентов в БД отлично от количества полученных клиентов",
                allClientFromService,
                allClientFromDb);
    }

    @DisplayName("Получение клиента по номеру телефона")
    @Test
    public void checkSuccessGetClientByPhoneNumber() {
        String testClientMobilePhone = "79991232233";
        Client receivedFromServiceClient = clientService.getClientByPhone(testClientMobilePhone);
        log.info("Найденные клиент" + receivedFromServiceClient);
        assertEquals("Номер телефона найденного клиента не соответствует искомому",
                testClientMobilePhone,
                receivedFromServiceClient.getContactInfo().getMobilePhone());
    }

    @DisplayName("Успешное добавление клиента")
    @Test
    public void checkSuccessClientAdd() {
        clientService.addClient(testClient);
        Client clientReceivedByService = clientService.getClientByPhone(testClient.getContactInfo().getMobilePhone());
        assertEquals("Номер телефона найденного клиента не соответствует искомому",
                testClient.getContactInfo().getMobilePhone(),
                clientReceivedByService.getContactInfo().getMobilePhone());
    }
}



