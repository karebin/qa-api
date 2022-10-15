package ru.test.data.manager.api.controllers.clients;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.test.data.manager.api.helper.ResourceHelper;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = ClientControllerIT.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {ClientControllerIT.Initializer.class})
@Testcontainers
@ComponentScan({"ru.test.data.manager.api"})
@AutoConfigureMockMvc
public class ClientControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Container
    public static PostgreSQLContainer postgres = (PostgreSQLContainer) new PostgreSQLContainer("postgres:alpine")
            .withDatabaseName("postgresTest")
            .withUsername("postgresTest")
            .withPassword("postgresTest")
            .withInitScript("db.sql");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext);
            System.out.println(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    @DisplayName("Получение клиента (с продуктами) по номеру телефона")
    void getClientByPhoneNumberWithProductTest() throws Exception {
        String actual = ResourceHelper.getFixtureFromResource("fixture/getClientWithProduct.json");
        MvcResult result = mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber=79991232232")).andReturn();
        assertEquals(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                actual);
    }

    @Test
    @DisplayName("Получение клиента (без продуктов) по номеру телефона")
    void getClientByPhoneNumberWithoutProductTest() throws Exception {
        String actual = ResourceHelper.getFixtureFromResource("fixture/getClientWithoutProduct.json");
        MvcResult result = mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber=79991232237")).andReturn();
        assertEquals(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                actual);
    }

    @Test
    @DisplayName("Получение клиента (без фамилии) по номеру телефона")
    void getClientByPhoneNumberWithoutLastNameTest() throws Exception {
        String actual = ResourceHelper.getFixtureFromResource("fixture/getClientWithoutLastName.json");
        MvcResult result = mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber=79991232236")).andReturn();
        assertEquals(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                actual);
    }

    @Test
    @DisplayName("Получение клиента (без Email) по номеру телефона")
    void getClientByPhoneNumberWithoutEmailTest() throws Exception {
        String actual = ResourceHelper.getFixtureFromResource("fixture/getClientWithoutEmail.json");
        MvcResult result = mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber=79991232233")).andReturn();
        assertEquals(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                actual);
    }

    @Test
    @DisplayName("Получение случайного клиента по номеру телефона")
    void getRandomClientTest() throws Exception {
        this.mockMvc.perform(get("/getRandomClients"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.contactInfo.mobilePhone").isNotEmpty());
    }

    @Test
    void getAllClientsTest() throws Exception {
        String actual = ResourceHelper.getFixtureFromResource("fixture/getAllClients.json");
        MvcResult result = mockMvc.perform(get("/getAllClients")).andReturn();
        assertEquals(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                actual);
    }
}