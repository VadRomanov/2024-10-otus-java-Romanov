package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.util.List;

public class ResourcesFileLoader implements Loader {

  private final String fileName;
  private final ObjectMapper mapper;

  public ResourcesFileLoader(String fileName) {
    this.fileName = fileName;
    this.mapper = JsonMapper.builder().build();
  }

  @Override
  public List<Measurement> load() {
    // читает файл, парсит и возвращает результат
    var inputStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName);
    try {
      return mapper.readValue(inputStream, new TypeReference<>() {});
    } catch (IOException e) {
      throw new FileProcessException(e);
    }
  }
}
