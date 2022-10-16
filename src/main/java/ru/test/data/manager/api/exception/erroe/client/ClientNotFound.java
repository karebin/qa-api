package ru.test.data.manager.api.exception.erroe.client;

public class ClientNotFound extends RuntimeException {
    public ClientNotFound(String message){
        super(message);
    }
}
