package ru.test.data.manager.api.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.test.data.manager.api.models.client.Client;

import ru.test.data.manager.api.models.client.ClientWithOutProducts;
import ru.test.data.manager.api.services.client.ClientService;

import java.util.List;

@RestController
public class ClientsController {

    @Autowired
    ClientService clientService;

    @ApiOperation(value = "Получить случайного клиента клиента", notes = "Получить случайного клиента")
    @GetMapping("/getRandomClients")
    public Client getRandomClient() {
        return ((Client) clientService.getRandomClient());
    }

    @ApiOperation(value = "Получить всех клиентов", notes = "Получить всех клиентов")
    @GetMapping("/getAllClients")
    public List<Client> getAllClient() {
        return clientService.getAllClient();
    }

    @ApiOperation(value = "Получить клиента", notes = "Получить клиента по номеру телефона")
    @ApiImplicitParam(name = "phoneNumber", required = true)
    @GetMapping("/getClientByPhoneNumber")
    public Client getClientByPhoneNumber(@RequestParam String phoneNumber) {
        return clientService.getClientByPhone(phoneNumber);
    }

    @ApiOperation(value = "Создать клиента", notes = "Создать клиента")
    @PutMapping("/addClient")
    public Client addClient(@RequestBody ClientWithOutProducts client) {
        return clientService.addClient(client);
    }
}
