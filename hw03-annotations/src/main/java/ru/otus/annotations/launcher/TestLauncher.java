package ru.otus.annotations.launcher;

import ru.otus.annotations.model.TestModel;
import ru.otus.annotations.annotation.After;
import ru.otus.annotations.annotation.Before;
import ru.otus.annotations.annotation.Test;
import ru.otus.annotations.exception.ConstructorNotFound;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestLauncher {

    private static final String TEST_FAILED_MESSAGE_TEMPLATE = "%s failed\n";
    private static final String TEST_RESULT_MESSAGE_TEMPLATE = """
            
            Tests passed: %d
            Tests failed: %d
            Number of tests: %d
            """;

    public static void launchTest(String fullClassName) {
        try {
            Class<?> testClass = TestLauncher.class.getClassLoader().loadClass(fullClassName);
            List<Method> testMethods = detectMethodsMarkedByGivenAnnotation(testClass, Test.class);
            TestModel testModel = TestModel.builder()
                    .constructor(detectConstructorWithoutParameters(testClass))
                    .beforeMethods(detectMethodsMarkedByGivenAnnotation(testClass, Before.class))
                    .afterMethods(detectMethodsMarkedByGivenAnnotation(testClass, After.class))
                    .build();
            long successTests = testMethods.stream()
                    .filter(method -> {
                        boolean testResult = runTest(method, testModel);
                        if (!testResult) {
                            String testName = method.getAnnotation(Test.class).name();
                            System.out.printf(String.format(TEST_FAILED_MESSAGE_TEMPLATE, testName));
                        }
                        System.out.println("-----------------");
                        return testResult;
                    })
                    .count();
            System.out.printf(TEST_RESULT_MESSAGE_TEMPLATE,
                    successTests, testMethods.size() - successTests, testMethods.size());
        } catch (ClassNotFoundException exception) {
            System.out.println("invalid classname");
        }
    }

    private static Constructor<?> detectConstructorWithoutParameters(Class<?> clazz) {
        try {
            final Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException exception) {
            throw new ConstructorNotFound("Constructor not found, impossible to run test");
        }
    }

    private static List<Method> detectMethodsMarkedByGivenAnnotation(
            Class<?> clazz,
            Class<? extends Annotation> annotationType
    ) {
        return Stream.of(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(annotationType))
                .peek(method -> method.setAccessible(true))
                .collect(Collectors.toList());
    }


    private static Optional<Object> createInstance(Constructor constructor) {
        try {
            return Optional.of(constructor.newInstance());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    private static boolean runTest(Method testMethod, TestModel testModel) {
        Optional<Object> instance = createInstance(testModel.getConstructor());
        if (instance.isEmpty()) {
            return false;
        }
        if (!runMethods(testModel.getBeforeMethods(), instance.get())) {
            runMethods(testModel.getAfterMethods(), instance.get());
            return false;
        }
        if (!runMethod(testMethod, instance.get())) {
            runMethods(testModel.getAfterMethods(), instance.get());
            return false;
        }
        return runMethods(testModel.getAfterMethods(), instance.get());
    }

    private static boolean runMethods(List<Method> methods, Object testInstance) {
        Optional<Method> failedMethod = methods.stream()
                .filter(method -> runMethod(method, testInstance))
                .findFirst();
        return failedMethod.isPresent();
    }

    private static boolean runMethod(Method method, Object testInstance) {
        try {
            method.setAccessible(true);
            method.invoke(testInstance);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
