package ru.otus.annotations.model;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

@Getter
@Builder
public class TestModel {

    private List<Method> beforeMethods;
    private List<Method> afterMethods;
    private Constructor<?> constructor;

}
