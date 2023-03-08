package ru.otus.logger;

import ru.otus.logger.annotation.Log;

public class TestClass {

    @Log
    public String toString(Object testClass) {
        return testClass.toString();
    }

    @Log
    public double calculate(long x, double y) {
        return x + y;
    }

    @Log
    public long calculate(int x, long y, int z) {
        return x + y + z;
    }


    @Log
    public String doSomething() {
        return "Test";
    }

}
