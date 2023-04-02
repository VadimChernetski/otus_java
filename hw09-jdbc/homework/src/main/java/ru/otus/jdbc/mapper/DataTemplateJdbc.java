package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.exception.InvalidModelException;
import ru.otus.jdbc.exception.MapperException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
@SuppressWarnings("unchecked")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

  private final DbExecutor dbExecutor;
  private final EntitySQLMetaData entitySQLMetaData;
  private final EntityClassMetaData<?> entityClassMetaData;

  public DataTemplateJdbc(DbExecutor dbExecutor,
                          EntitySQLMetaData entitySQLMetaData,
                          EntityClassMetaData<?> entityClassMetaData) {
    this.dbExecutor = dbExecutor;
    this.entitySQLMetaData = entitySQLMetaData;
    this.entityClassMetaData = entityClassMetaData;
  }

  @Override
  public Optional<T> findById(Connection connection, long id) {
    return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id),
      resultSet -> {
        try {
          if (resultSet.next()) {
            T object = (T) entityClassMetaData.getConstructor().newInstance();
            entityClassMetaData.getAllFields().forEach(field -> setField(object, field, resultSet));
            return object;
          }
          return null;
        } catch (SQLException exception) {
          throw new InvalidModelException(exception);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException exception) {
          throw new MapperException(exception);
        }
      });
  }

  @Override
  public List<T> findAll(Connection connection) {
    return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(),
      resultSet -> {
        try {
          List<T> objects = new ArrayList<>();
          while (resultSet.next()) {
            T object = (T) entityClassMetaData.getConstructor().newInstance();
            entityClassMetaData.getAllFields().forEach(field -> setField(object, field, resultSet));
            objects.add(object);
          }
          return objects;
        } catch (SQLException exception) {
          throw new InvalidModelException(exception);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException exception) {
          throw new MapperException(exception);
        }
      }).orElseThrow(() -> new RuntimeException("Unexpected error"));
  }

  @Override
  public long insert(Connection connection, T client) {
    var params = entityClassMetaData.getFieldsWithoutId().stream()
      .map(field -> getFieldValue(client, field))
      .toList();
    return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
  }

  @Override
  public void update(Connection connection, T client) {
    var params = entityClassMetaData.getAllFields().stream()
      .map(field -> getFieldValue(client, field))
      .toList();
    dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
  }

  private Object getFieldValue(T object, Field field) {
    var getter = entityClassMetaData.getGetters().stream()
      .filter(method -> method.getName().equalsIgnoreCase("get" + field.getName()))
      .findFirst()
      .orElseThrow(() -> new MapperException("no getter for field = " + field.getName()));
    Object value;
    try {
      value = getter.invoke(object);
    } catch (InvocationTargetException | IllegalAccessException exception) {
      throw new MapperException(exception);
    }
    return value;
  }

  private void setField(Object object, Field field, ResultSet resultSet) {
    var setter = entityClassMetaData.getSetters().stream()
      .filter(method -> method.getName().equalsIgnoreCase("set" + field.getName()))
      .findFirst()
      .orElseThrow(() -> new MapperException("no setter for field = " + field.getName()));
    try {
      var value = resultSet.getObject(field.getName());
      setter.invoke(object, value);
    } catch (SQLException exception) {
      throw new MapperException(exception);
    } catch (InvocationTargetException | IllegalAccessException exception) {
      throw new InvalidModelException(exception);
    }

  }
}
