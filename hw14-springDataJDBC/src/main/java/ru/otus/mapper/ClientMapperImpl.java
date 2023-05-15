package ru.otus.mapper;

import org.springframework.stereotype.Service;
import ru.otus.dto.ClientDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.stream.Collectors;

@Service
public class ClientMapperImpl implements ClientMapper {

  @Override
  public ClientDto toClientDto(Client client) {
    var phones = client.getPhones().stream()
      .map(Phone::getNumber)
      .collect(Collectors.toList());
    return ClientDto.builder()
      .id(client.getId())
      .name(client.getName())
      .street(client.getAddress().getStreet())
      .phones(phones)
      .build();
  }
}
