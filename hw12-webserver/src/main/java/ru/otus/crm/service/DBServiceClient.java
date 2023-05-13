package ru.otus.crm.service;

import ru.otus.crm.dto.ClientDto;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<ClientDto> findAll();
}
