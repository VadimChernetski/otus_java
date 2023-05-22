package ru.otus.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.ClientDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repostory.ClientRepository;
import ru.otus.mapper.ClientMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final ClientRepository clientRepository;

  private final ClientMapper clientMapper;

  @Override
  @Transactional
  public void saveClient(ClientDto clientDto) {

    var phones = clientDto.getPhones().stream()
      .map(Phone::new)
      .collect(Collectors.toSet());
    var address = new Address(clientDto.getStreet());

    var client = new Client(clientDto.getName(), address, phones);

    var savedClient = clientRepository.save(client);
  }


  @Override
  public List<ClientDto> findAll() {

    var clients = clientRepository.findAll();

    return StreamSupport.stream(clients.spliterator(), false)
      .map(clientMapper::toClientDto)
      .toList();
  }
}
