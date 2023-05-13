package ru.otus.crm.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientDto {

  private long id;
  private String name;
  private String street;
  private List<String> phones;

}
