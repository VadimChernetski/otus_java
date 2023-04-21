package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exception.ComponentCreationException;
import ru.otus.appcontainer.exception.NoSuchComponentException;

import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

  private final List<Object> appComponents = new ArrayList<>();
  private final Map<String, Object> appComponentsByName = new HashMap<>();

  public List<Object> getAppComponents() {
    return appComponents;
  }

  public Map<String, Object> getAppComponentsByName() {
    return appComponentsByName;
  }

  public AppComponentsContainerImpl(Class<?> initialConfigClass) {
    processConfig(initialConfigClass);
  }

  public AppComponentsContainerImpl(Class<?>... configClasses) {
    for (Class<?> configClass : configClasses) {
      processConfig(configClass);
    }
  }

  public AppComponentsContainerImpl(String classPath) {
    handleConfigurationsInClassPath(classPath);
    handleComponentsInClassPath(classPath);
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
    new Reflections(classPath).getTypesAnnotatedWith(AppComponentsContainerConfig.class).stream()
      .sorted((firstConfig, secondConfig) -> {
        var firstAnnotation =
          firstConfig.getAnnotation(AppComponentsContainerConfig.class);
        var secondAnnotation =
          secondConfig.getAnnotation(AppComponentsContainerConfig.class);
        return Integer.compare(firstAnnotation.order(), secondAnnotation.order());
      })
      .forEach(this::handConfigurationClass);
  }

  private void handleComponentsInClassPath(String classPath) {
    new Reflections(classPath).getTypesAnnotatedWith(AppComponent.class).stream()
      .sorted((firstConfig, secondConfig) -> {
        var firstAnnotation =
          firstConfig.getAnnotation(AppComponent.class);
        var secondAnnotation =
          secondConfig.getAnnotation(AppComponent.class);
        return Integer.compare(firstAnnotation.order(), secondAnnotation.order());
      })
      .forEach(clazz -> {
        var componentName = clazz.getAnnotation(AppComponent.class).name();
        if (appComponentsByName.containsKey(componentName)) {
          throw new ComponentCreationException("component with name: " + componentName + " exists");
        }
        var classInstance = createClassInstance(clazz);
        appComponents.add(classInstance);
        appComponentsByName.put(componentName, classInstance);
      });
  }

  private <T> void handConfigurationClass(Class<T> configClass) {
    var classInstance = createClassInstance(configClass);
    Arrays.stream(configClass.getDeclaredMethods())
      .filter(method -> method.isAnnotationPresent(AppComponent.class))
      .sorted((firstMethod, secondMethod) -> {
        var firstAnnotation = firstMethod.getAnnotation(AppComponent.class);
        var secondAnnotation = secondMethod.getAnnotation(AppComponent.class);
        return Integer.compare(firstAnnotation.order(), secondAnnotation.order());
      })
      .forEach(method -> {
        var componentName = method.getAnnotation(AppComponent.class).name();
        if (appComponentsByName.containsKey(componentName)) {
          throw new ComponentCreationException("component with name: " + componentName + " exists");
        }
        var objects = Arrays.stream(method.getParameterTypes())
          .map(this::getAppComponent)
          .toArray();
        method.setAccessible(true);
        try {
          var component = method.invoke(classInstance, objects);
          appComponents.add(component);
          appComponentsByName.put(componentName, component);
        } catch (Exception e) {
          throw new ComponentCreationException(e.getMessage());
        }
      });
  }

  public <T> T createClassInstance(Class<T> clazz) {
    var constructors = clazz.getConstructors();
    if (constructors.length != 1) {
      throw new ComponentCreationException("incorrect amount of constructors");
    }
    var constructor = constructors[0];
    constructor.setAccessible(true);
    var parameters = Arrays.stream(constructor.getParameterTypes())
      .map(this::getAppComponent)
      .toArray();
    try {
      return (T) constructor.newInstance(parameters);
    } catch (Exception e) {
      throw new ComponentCreationException(e.getMessage());
    }
  }

}
