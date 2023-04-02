package ru.otus.jdbc.type;

public enum AccessorType {

  SETTER("set"),
  GETTER("get");

  private final String value;

  AccessorType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
