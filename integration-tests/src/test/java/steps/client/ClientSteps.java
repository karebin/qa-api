package steps.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import data.Params;
import data.models.client.Client;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Тогда;
import io.cucumber.junit.CucumberOptions;

import java.util.List;

import static org.junit.Assert.assertTrue;


@CucumberOptions(plugin = {"pretty", "html:target/cucumber"})
public class ClientSteps extends Params {

    @Дано("Запрощены все клиенты в системе")
    public void givenAllClients() {
        response = restTemplate.getForEntity(
                baseUrl + "getAllClients",
                String.class);
    }

    @Тогда("Возвращено более одного клиента")
    public void checkClientCount() throws JsonProcessingException {
        List<Client> clientList = mapper.readValue(response.getBody(), new TypeReference<List<Client>>() {
        });
        System.out.println(clientList.toString());
        assertTrue(clientList.size() > 2);
    }
}

