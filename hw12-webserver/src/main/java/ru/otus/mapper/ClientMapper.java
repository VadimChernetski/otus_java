package ru.otus.mapper;

import ru.otus.crm.dto.ClientDto;
import ru.otus.crm.model.Client;

public interface ClientMapper {

  Client toClient(ClientDto clientDto);
  ClientDto toClientDto(Client client);

}
