package ru.test.data.manager.api.services.client;

import org.springframework.stereotype.Service;
import ru.test.data.manager.api.models.client.Client;
import ru.test.data.manager.api.models.client.ClientWithOutProducts;

import java.util.List;

@Service
public interface ClientService {

    // Read operation
    Client getRandomClient();

    List<Client> getAllClient();

    Client getClientByPhone(String phoneNumber);

    Client addClient(ClientWithOutProducts client);

}
