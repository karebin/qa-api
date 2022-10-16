package ru.test.data.manager.api.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.test.data.manager.api.entity.ClientEntity;
import ru.test.data.manager.api.exception.erroe.client.ClientNotFound;
import ru.test.data.manager.api.models.client.Client;
import ru.test.data.manager.api.models.client.ClientWithOutProducts;
import ru.test.data.manager.api.models.client.ContactInfo;
import ru.test.data.manager.api.repository.client.ClientRepository;
import ru.test.data.manager.api.services.product.ProductService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Client getRandomClient() {
        List<Client> clients = clientEntityListToClientList(clientRepository.findAll());
        clients.forEach(c -> {
                    c.setProducts(productService.getClientProductList((int) c.getId()));
                }
        );
        return clients.get((int) (Math.random() * clients.size()));
    }

    @Override
    public List<Client> getAllClient() {
        List<Client> clients = clientEntityListToClientList(clientRepository.findAll());
        clients.forEach(c -> {
                    c.setProducts(productService.getClientProductList((int) c.getId()));
                }
        );
        return clients;
    }

    @Override
    public Client getClientByPhone(String phoneNumber) {
        if (phoneNumber.isEmpty()) throw new ClientNotFound("Client phone number can not empty");
        clientRepository.findByMobilePhone(phoneNumber);
        try {
            Client client = clientEntityToClient(clientRepository.findByMobilePhone(phoneNumber));
            client.setProducts(productService.getClientProductList((int) client.getId()));
            return client;
        } catch (RuntimeException e) {
            throw new ClientNotFound("Client not found by phone number: " + phoneNumber);
        }
    }

    private ClientEntity getClientEntityByPhoneNumber(String phoneNumber) {
        return clientRepository.findByMobilePhone(phoneNumber);
    }

    @Override
    public Client addClient(ClientWithOutProducts client) {
        return clientEntityToClient(clientRepository.save(clientToClientEntity(client)));
    }

    private List<Client> clientEntityListToClientList(List<ClientEntity> clientEntity) {
        List<Client> clientList = new ArrayList<>();
        clientEntity.forEach(ce -> {
                    clientList.add(clientEntityToClient(ce));
                }
        );
        return clientList;
    }

    private Client clientEntityToClient(ClientEntity clientEntity) {
        return Client.builder()
                .id(clientEntity.getId())
                .firstName(clientEntity.getFirstName())
                .lastName(clientEntity.getLastName())
                .contactInfo(ContactInfo.builder()
                        .mobilePhone(clientEntity.getMobilePhone())
                        .email(clientEntity.getEmail())
                        .build())
                .build();
    }

    private ClientEntity clientToClientEntity(ClientWithOutProducts client) {
        return ClientEntity.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .mobilePhone(client.getContactInfo().getMobilePhone())
                .email(client.getContactInfo().getEmail())
                .build();
    }

    private List<Client> setProductToClient() {
        List<Client> clients = getAllClient();
        clients.forEach(c -> {
                    c.setProducts(productService.getClientProductList((int) c.getId()));
                }
        );
        return clients;
    }

}

