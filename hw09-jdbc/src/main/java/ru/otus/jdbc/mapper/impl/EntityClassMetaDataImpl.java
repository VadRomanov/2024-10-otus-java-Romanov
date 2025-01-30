package ru.otus.jdbc.mapper.impl;

import ru.otus.core.annotation.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
  private final String name;
  private final Constructor<T> constructor;
  private final Field idField;
  private final List<Field> allFields;
  private final List<Field> fieldsWithoutId;

  public EntityClassMetaDataImpl(Class<T> clazz) throws NoSuchMethodException {
    name = clazz.getSimpleName();
    constructor = clazz.getConstructor();
    allFields = List.of(clazz.getDeclaredFields());
    fieldsWithoutId = allFields.stream().filter(f -> !f.isAnnotationPresent(Id.class)).toList();
    idField = allFields.stream().filter(f -> f.isAnnotationPresent(Id.class)).findFirst()
        .orElseThrow(() -> new RuntimeException("field annotated with @Id is missing"));
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Constructor<T> getConstructor() {
    return constructor;
  }

  @Override
  public Field getIdField() {
    return idField;
  }

  @Override
  public List<Field> getAllFields() {
    return allFields;
  }

  @Override
  public List<Field> getFieldsWithoutId() {
    return fieldsWithoutId;
  }
}
