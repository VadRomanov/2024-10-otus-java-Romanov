package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

  private final String fileName;
  private final ObjectMapper mapper;

  public FileSerializer(String fileName) {
    this.fileName = fileName;
    this.mapper = JsonMapper.builder().build();
  }

  @Override
  public void serialize(Map<String, Double> data) {
    // формирует результирующий json и сохраняет его в файл
    var file = new File(fileName);
    try {
      mapper.writeValue(file, data);
    } catch (IOException e) {
      throw new FileProcessException(e);
    }
  }
}
