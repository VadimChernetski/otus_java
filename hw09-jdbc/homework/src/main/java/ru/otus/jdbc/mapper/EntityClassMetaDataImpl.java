package ru.otus.jdbc.mapper;

import ru.otus.annotation.Id;
import ru.otus.jdbc.exception.InvalidModelException;
import ru.otus.jdbc.type.AccessorType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

  private final Class<T> modelClass;

  private Constructor<T> constructor;
  private Field id;
  private List<Field> allFields;

  private List<Field> fieldsWithoutId;
  private List<Method> setters;

  private List<Method> getters;

  public EntityClassMetaDataImpl(Class<T> modelClass) {
    this.modelClass = modelClass;
    initConstructor();
    initId();
  }


  @Override
  public String getName() {
    return modelClass.getSimpleName().toLowerCase();
  }

  @Override
  public Constructor<T> getConstructor() {
    return constructor;
  }

  @Override
  public Field getIdField() {
    return id;
  }

  @Override
  public List<Field> getAllFields() {
    if (allFields == null) {
      allFields = Stream.of(modelClass.getDeclaredFields())
        .toList();
    }
    return allFields;
  }

  @Override
  public List<Field> getFieldsWithoutId() {
    if (fieldsWithoutId == null) {
      fieldsWithoutId = Stream.of(modelClass.getDeclaredFields())
        .filter(field -> !field.isAnnotationPresent(Id.class))
        .toList();
    }
    return fieldsWithoutId;
  }

  @Override
  public List<Method> getSetters() {
    if (setters == null) {
      setters = getAccessorMethod(AccessorType.SETTER);
    }
    return setters;
  }

  @Override
  public List<Method> getGetters() {
    if (getters == null) {
      getters = getAccessorMethod(AccessorType.GETTER);
    }
    return getters;
  }

  private void initConstructor() {
    try {
      constructor = modelClass.getConstructor();
    } catch (NoSuchMethodException exception) {
      throw new InvalidModelException("no default constructor", exception);
    }
  }

  private List<Method> getAccessorMethod(AccessorType accessorType) {
    return Stream.of(modelClass.getDeclaredMethods())
      .filter(method ->
        getAllFields().stream()
          .anyMatch(field -> method.getName().equalsIgnoreCase(accessorType.getValue() + field.getName())))
      .collect(Collectors.toList());
  }

  private void initId() {
    var ids = Stream.of(modelClass.getDeclaredFields())
      .filter(field -> field.isAnnotationPresent(Id.class))
      .toList();
    if (ids.size() > 1) {
      throw new InvalidModelException("id field should be single");
    }
    if (ids.isEmpty()) {
      throw new InvalidModelException("no id");
    }
    id = ids.get(0);
  }

}
