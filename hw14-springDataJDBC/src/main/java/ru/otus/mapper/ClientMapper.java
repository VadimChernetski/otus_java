package ru.otus.mapper;

import ru.otus.dto.ClientDto;
import ru.otus.model.Client;

public interface ClientMapper {

  ClientDto toClientDto(Client client);

}
