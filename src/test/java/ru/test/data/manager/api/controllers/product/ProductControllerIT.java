package ru.test.data.manager.api.controllers.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.test.data.manager.api.db.TestContainerPostgres;
import ru.test.data.manager.api.helper.ResourceHelper;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = ProductControllerIT.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {TestContainerPostgres.class})
@Testcontainers
@Transactional
@ComponentScan({"ru.test.data.manager.api"})
@AutoConfigureMockMvc

public class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj;

    String resourcesProductRequestFixture = "fixture/request/product/";

    public static Object[][] checkAddProductError() {
        return new String[][]{
                {"addProductWithNegativeBalanceRequest.json", "Balance cannot be negative or more that 4 000 000"},
                {"addProductWithSoMuchBalanceRequest.json", "Balance cannot be negative or more that 4 000 000"},
                {"addProductWithOutTypeRequest.json", "Fields: Type or ProductType is empty"},
                {"addProductWithOutTypeRequest.json", "Fields: Type or ProductType is empty"},
                {"addProductWithOutTypeRequest.json", "Fields: Type or ProductType is empty"}
        };
    }

    @Test
    @Rollback
    @DisplayName("Добавление продукта клиенту")
    public void addProductToClient() throws Exception {
        String expected = ResourceHelper.getFixtureFromResource("fixture/response/product/addProduct.json");
        String request = ResourceHelper.getFixtureFromResource("fixture/request/product/addProductRequest.json");

        actualObj = mapper.readTree(request);

        MvcResult result = mockMvc
                .perform(put("/addProductByClientId?clientId=10")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(String.valueOf(actualObj))).andReturn();

        assertEquals(expected, result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @ParameterizedTest()
    @DisplayName("Получение ошибок для параметров {balance<0, balance>4000000, type, productType is null/empty}")
    @MethodSource("checkAddProductError")
    public void dataProviderTest(String requestFixture, String errorMessage) throws Exception {
        String request = ResourceHelper.getFixtureFromResource(resourcesProductRequestFixture + requestFixture);

        actualObj = mapper.readTree(request);

        mockMvc.perform(put("/addProductByClientId?clientId=10")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(String.valueOf(actualObj)))
                .andExpect(jsonPath("$.message").value
                        (errorMessage));
    }

    @Test
    @DisplayName("Получение ошибки при добавдении продукта клиенту не заведенному в системе")
    public void addProductToEmptyClient() throws Exception {
        String request = ResourceHelper.getFixtureFromResource("fixture/request/product/addProductRequest.json");

        actualObj = mapper.readTree(request);

        mockMvc.perform(put("/addProductByClientId?clientId=999")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(String.valueOf(actualObj)))
                .andExpect(jsonPath("$.message").value
                        ("Client for add product not found"));
    }
}