package ru.otus.service;


import ru.otus.dto.ClientDto;
import ru.otus.model.Client;

import java.util.List;

public interface ClientService {

    void saveClient(ClientDto client);

    List<ClientDto> findAll();
}
