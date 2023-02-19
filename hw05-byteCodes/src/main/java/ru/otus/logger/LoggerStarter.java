package ru.otus.logger;


//command to start: java -javaagent:logger.jar -jar logger.jar

public class LoggerStarter {
    public static void main(String[] args) {
        final TestClass testClass = new TestClass();
        System.out.println(testClass.calculate(2, 3.8));
        System.out.println(testClass.calculate(3, 5, 90));
        System.out.println(testClass.toString(new Object()));
        System.out.println(testClass.doSomething());
    }

}
