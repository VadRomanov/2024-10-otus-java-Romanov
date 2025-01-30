package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

  private final DbExecutor dbExecutor;
  private final EntitySQLMetaData entitySQLMetaData;
  private final EntityClassMetaData<T> entityClassMetaData;

  public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
    this.dbExecutor = dbExecutor;
    this.entitySQLMetaData = entitySQLMetaData;
    this.entityClassMetaData = entityClassMetaData;
  }

  @Override
  public Optional<T> findById(Connection connection, long id) {
    return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
      try {
        if (rs.next()) {
          return createAndFillInstance(rs);
        }
        return null;
      } catch (SQLException e) {
        throw new DataTemplateException(e);
      }
    });
  }

  @Override
  public List<T> findAll(Connection connection) {
    return dbExecutor
        .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
          var result = new ArrayList<T>();
          try {
            while (rs.next()) {
              result.add(createAndFillInstance(rs));
            }
            return result;
          } catch (SQLException e) {
            throw new DataTemplateException(e);
          }
        })
        .orElseThrow(() -> new RuntimeException("Unexpected error"));
  }

  @Override
  public long insert(Connection connection, T instance) {
    try {
      return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
          prepareParams(instance));
    } catch (Exception e) {
      throw new DataTemplateException(e);
    }
  }

  @Override
  public void update(Connection connection, T instance) {
    try {
      dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
          prepareParamsForUpdate(instance));
    } catch (Exception e) {
      throw new DataTemplateException(e);
    }
  }


  private T createAndFillInstance(ResultSet rs) {
    try {
      Constructor<T> constructor = entityClassMetaData.getConstructor();
      constructor.setAccessible(true);
      T instance = constructor.newInstance();
      for (Field field : entityClassMetaData.getAllFields()) {
        field.setAccessible(true);
        field.set(instance, rs.getObject(field.getName()));
      }
      return instance;
    } catch (Exception e) {
      throw new DataTemplateException(e);
    }
  }

  private List<Object> prepareParams(T instance) {
    List<Field> fields = entityClassMetaData.getFieldsWithoutId();
    return fields.stream().map(f -> {
      try {
        f.setAccessible(true);
        return f.get(instance);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }).toList();
  }

  private List<Object> prepareParamsForUpdate(T instance) {
    List<Object> params = new ArrayList<>(prepareParams(instance));
    Field idField = entityClassMetaData.getIdField();
    try {
      idField.setAccessible(true);
      params.add(idField.get(instance));
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return params;
  }
}
