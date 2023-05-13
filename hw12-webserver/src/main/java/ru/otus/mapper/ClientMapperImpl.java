package ru.otus.mapper;

import ru.otus.crm.dto.ClientDto;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.stream.Collectors;

public class ClientMapperImpl implements ClientMapper {
  @Override
  public Client toClient(ClientDto clientDto) {
    var phones = clientDto.getPhones().stream()
      .map(number -> new Phone(null, number))
      .collect(Collectors.toList());
    return Client.builder()
      .address(new Address(null, clientDto.getStreet()))
      .phones(phones)
      .name(clientDto.getName())
      .build();
  }

  @Override
  public ClientDto toClientDto(Client client) {
    var phones = client.getPhones().stream()
      .map(Phone::getNumber)
      .collect(Collectors.toList());
    return ClientDto.builder()
      .name(client.getName())
      .street(client.getAddress().getStreet())
      .phones(phones)
      .build();
  }
}
