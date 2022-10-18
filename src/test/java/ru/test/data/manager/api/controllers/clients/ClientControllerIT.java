package ru.test.data.manager.api.controllers.clients;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.test.data.manager.api.db.TestContainerPostgres;
import ru.test.data.manager.api.helper.ResourceHelper;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = ClientControllerIT.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {TestContainerPostgres.class})
@Testcontainers
@ComponentScan({"ru.test.data.manager.api"})
@AutoConfigureMockMvc
public class ClientControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Получение клиента (с продуктами) по номеру телефона")
    void getClientByPhoneNumberWithProductTest() throws Exception {
        String expected = ResourceHelper.getFixtureFromResource("fixture/response/client/getClientWithProduct.json");
        MvcResult result = mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber=79991232232")).andReturn();
        assertEquals(expected, result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Получение клиента (без продуктов) по номеру телефона")
    void getClientByPhoneNumberWithoutProductTest() throws Exception {
        String expected = ResourceHelper.getFixtureFromResource("fixture/response/client/getClientWithoutProduct.json");
        MvcResult result = mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber=79991232237")).andReturn();
        assertEquals(expected, result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Получение клиента (без фамилии) по номеру телефона")
    void getClientByPhoneNumberWithoutLastNameTest() throws Exception {
        String expected = ResourceHelper.getFixtureFromResource("fixture/response/client/getClientWithoutLastName.json");
        MvcResult result = mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber=79991232236")).andReturn();
        assertEquals(expected, result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Получение клиента (без Email) по номеру телефона")
    void getClientByPhoneNumberWithoutEmailTest() throws Exception {
        String expected = ResourceHelper.getFixtureFromResource("fixture/response/client/getClientWithoutEmail.json");
        MvcResult result = mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber=79991232233")).andReturn();
        assertEquals(expected, result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Получение случайного клиента по номеру телефона")
    void getRandomClientTest() throws Exception {
        mockMvc.perform(get("/getRandomClients"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.contactInfo.mobilePhone").isNotEmpty());
    }

    @Test
    @DisplayName("Получение всех клиентов")
    void getAllClientsTest() throws Exception {
        String expected = ResourceHelper.getFixtureFromResource("fixture/response/client/getAllClients.json");
        MvcResult result = mockMvc.perform(get("/getAllClients")).andReturn();
        assertEquals(expected, result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Получение корректной ошибки {message} в случае если в БД нет клиента по {PhoneNumber}")
    void checkClientNotFoundError() throws Exception {
        mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber=79991232288"))
                .andExpect(jsonPath("$.message").value
                        ("Client not found by phone number: 79991232288"));
    }

    @Test
    @DisplayName("Получение корректной ошибки {message} в случае если не указан номер телефона для поска клиента {PhoneNumber}")
    void checkPhoneNumberIsEmptyError() throws Exception {
        mockMvc.perform(get("/getClientByPhoneNumber?phoneNumber="))
                .andExpect(jsonPath("$.message").value
                        ("Client phone number can not empty"));
    }
}