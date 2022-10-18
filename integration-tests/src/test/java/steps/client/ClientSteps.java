package steps.client;

import io.cucumber.java.ru.Дано;

public class ClientSteps {
    @Дано("Say Hello")
    public void givenAccountBalance() {
        System.out.println("Hello");
    }
}