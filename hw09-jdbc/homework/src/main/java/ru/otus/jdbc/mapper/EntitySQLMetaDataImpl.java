package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

  private final EntityClassMetaData<?> entityClassMetaData;

  public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
    this.entityClassMetaData = entityClassMetaData;
  }

  @Override
  public String getSelectAllSql() {
    return String.format("select %s from %s", getFieldsToSelect(), entityClassMetaData.getName());
  }

  @Override
  public String getSelectByIdSql() {
    return String.format(
      "select %s from %s where %s = ?",
      getFieldsToSelect(),
      entityClassMetaData.getName(),
      entityClassMetaData.getIdField().getName());
  }

  @Override
  public String getInsertSql() {
    return String.format(
      "insert into %s(%s) values (%s)",
      entityClassMetaData.getName(),
      getFieldsToInsert(),
      getWildcardsForInsert());
  }

  @Override
  public String getUpdateSql() {
    return String.format(
      "update %s set %s where %s = ?",
      entityClassMetaData.getName(),
      getFieldsWthWildcards(),
      entityClassMetaData.getIdField().getName()
    );
  }

  private String getFieldsToSelect() {
    return entityClassMetaData.getAllFields().stream()
      .map(Field::getName)
      .collect(Collectors.joining(", "));
  }

  private String getFieldsToInsert() {
    return entityClassMetaData.getFieldsWithoutId().stream()
      .map(Field::getName)
      .collect(Collectors.joining(", "));
  }

  private String getWildcardsForInsert() {
    return Stream.iterate("?", UnaryOperator.identity())
      .limit(entityClassMetaData.getFieldsWithoutId().size())
      .collect(Collectors.joining(", "));
  }

  private String getFieldsWthWildcards() {
    return entityClassMetaData.getFieldsWithoutId().stream()
      .map(field -> field.getName() + " = ?")
      .collect(Collectors.joining(", "));
  }

}
