package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class FileSerializer implements Serializer {

  private final Path path;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public FileSerializer(String fileName) {
    path = Paths.get(fileName);
  }

  @Override
  public void serialize(Map<String, Double> data) {
    try {
      var result = objectMapper.writeValueAsString(data);
      Files.write(path, result.getBytes());
    } catch (IOException e) {
      throw new FileProcessException(e);
    }
  }
}
