package ru.otus.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.ClientDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repostory.AddressRepository;
import ru.otus.repostory.ClientRepository;
import ru.otus.mapper.ClientMapper;
import ru.otus.repostory.PhoneRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final ClientRepository clientRepository;
  private final PhoneRepository phoneRepository;
  private final AddressRepository addressRepository;

  private final ClientMapper clientMapper;

  @Override
  @Transactional
  public void saveClient(ClientDto clientDto) {

    var client = Client.builder()
      .name(clientDto.getName())
      .address(Address.builder().street(clientDto.getStreet()).build())
      .build();

    var savedClient = clientRepository.save(client);

    clientDto.getPhones().stream()
      .map(phone -> Phone.builder()
        .clientId(savedClient.getId())
        .number(phone)
        .build())
      .forEach(phoneRepository::save);
  }


  @Override
  public List<ClientDto> findAll() {

    var clients = clientRepository.findAll();

    return StreamSupport.stream(clients.spliterator(), false)
      .map(clientMapper::toClientDto)
      .toList();
  }
}
