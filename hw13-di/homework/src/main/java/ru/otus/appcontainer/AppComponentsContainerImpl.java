package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exception.ComponentCreationException;
import ru.otus.appcontainer.exception.NoSuchComponentException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

  private final List<Object> appComponents = new ArrayList<>();
  private final Map<String, Object> appComponentsByName = new HashMap<>();

  public AppComponentsContainerImpl(Class<?> initialConfigClass) {
    processConfig(initialConfigClass);
  }

  public AppComponentsContainerImpl(Class<?>... configClasses) {
    handleConfigurations(Set.of(configClasses));
  }

  public AppComponentsContainerImpl(String classPath) {
    handleConfigurationsInClassPath(classPath);
  }

  private void processConfig(Class<?> configClass) {
    checkConfigClass(configClass);

    handConfigurationClass(configClass);
  }

  private void checkConfigClass(Class<?> configClass) {
    if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
      throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
    }
  }

  @Override
  public <C> C getAppComponent(Class<C> componentClass) {
    var components = appComponents.stream()
      .filter(component -> componentClass.isAssignableFrom(component.getClass()))
      .toList();
    if (components.size() > 1) {
      throw new IllegalArgumentException("expected single matching bean but found:" + components.size());
    }
    if (components.isEmpty()) {
      throw new NoSuchComponentException(componentClass.getSimpleName() + " not found");
    }
    return (C) components.get(0);
  }

  @Override
  public <C> C getAppComponent(String componentName) {
    if (!appComponentsByName.containsKey(componentName)) {
      throw new NoSuchComponentException("no component with name: " + componentName);
    }
    return (C) appComponentsByName.get(componentName);
  }

  private void handleConfigurationsInClassPath(String classPath) {
    var configurations = new Reflections(classPath)
      .getTypesAnnotatedWith(AppComponentsContainerConfig.class);
    handleConfigurations(configurations);
  }

  private void handleConfigurations(Set<Class<?>> configs) {
    configs.stream()
      .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
      .forEach(this::handConfigurationClass);
  }

  private <T> void handConfigurationClass(Class<T> configClass) {
    T classInstance;
    try {
      final Constructor<T> constructor = configClass.getConstructor();
      constructor.setAccessible(true);
      classInstance = constructor.newInstance();
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new ComponentCreationException(e);
    }
    Arrays.stream(configClass.getDeclaredMethods())
      .filter(method -> method.isAnnotationPresent(AppComponent.class))
      .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponent.class).order()))
      .forEach(method -> createComponentByMethodInvocation(classInstance, method));
  }

  private <T> void createComponentByMethodInvocation(T classInstance, Method method) {
    var componentName = method.getAnnotation(AppComponent.class).name();
    isComponentNameUnique(componentName);
    var objects = Arrays.stream(method.getParameterTypes())
      .map(this::getAppComponent)
      .toArray();
    method.setAccessible(true);
    try {
      var component = method.invoke(classInstance, objects);
      appComponents.add(component);
      appComponentsByName.put(componentName, component);
    } catch (Exception e) {
      throw new ComponentCreationException(e);
    }
  }

  private void isComponentNameUnique(String componentName) {
    if (appComponentsByName.containsKey(componentName)) {
      throw new ComponentCreationException("component with name: " + componentName + " exists");
    }
  }

}
