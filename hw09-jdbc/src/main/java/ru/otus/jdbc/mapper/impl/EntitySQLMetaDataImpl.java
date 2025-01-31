package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
  private final String selectAllSql;
  private final String selectByIdSql;
  private final String insertSql;
  private final String updateSql;

  public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
    String allFields = getAllFields(entityClassMetaData);
    String allFieldsExceptId = getAllFieldsExceptId(entityClassMetaData);
    String wildcardsForInsert = getWildcardsForInsert(entityClassMetaData);
    String wildcardsForUpdate = getWildcardsForUpdate(entityClassMetaData);

    selectAllSql = prepareSelectAllSql(allFields, entityClassMetaData.getName());
    selectByIdSql = prepareSelectByIdSql(allFields, entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    insertSql = prepareInsertSql(entityClassMetaData.getName(), allFieldsExceptId, wildcardsForInsert);
    updateSql = prepareUpdateSql(entityClassMetaData.getName(), wildcardsForUpdate, entityClassMetaData.getIdField().getName());
  }

  @Override
  public String getSelectAllSql() {
    return selectAllSql;
  }

  @Override
  public String getSelectByIdSql() {
    return selectByIdSql;
  }

  @Override
  public String getInsertSql() {
    return insertSql;
  }

  @Override
  public String getUpdateSql() {
    return updateSql;
  }

  private String prepareSelectAllSql(String allFields, String entityName) {
    return "select %s from %s".formatted(allFields, entityName);
  }

  private String prepareSelectByIdSql(String allFields, String entityName, String idName) {
    return "select %s from %s where %s = ?".formatted(allFields, entityName, idName);
  }

  private String prepareInsertSql(String entityName, String allFieldsExceptId, String wildcardsForInsert) {
    return "insert into %s(%s) values (%s)".formatted(entityName, allFieldsExceptId, wildcardsForInsert);
  }

  private String prepareUpdateSql(String entityName, String wildcardsForUpdate, String idName) {
    return "update %s set %s where %s = ?".formatted(entityName, wildcardsForUpdate, idName);
  }

  private String getAllFields(EntityClassMetaData<?> entityClassMetaData) {
    return entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(","));
  }

  private String getAllFieldsExceptId(EntityClassMetaData<?> entityClassMetaData) {
    return entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(","));
  }

  private String getWildcardsForInsert(EntityClassMetaData<?> entityClassMetaData) {
    StringJoiner joiner = new StringJoiner(",");
    for (int i = 0; i < entityClassMetaData.getFieldsWithoutId().size(); i++) {
      joiner.add("?");
    }
    return joiner.toString();
  }

  private String getWildcardsForUpdate(EntityClassMetaData<?> entityClassMetaData) {
    StringJoiner joiner = new StringJoiner("=?,", "", "=?");
    for (Field field : entityClassMetaData.getFieldsWithoutId()) {
      joiner.add(field.getName());
    }
    return joiner.toString();
  }

}
