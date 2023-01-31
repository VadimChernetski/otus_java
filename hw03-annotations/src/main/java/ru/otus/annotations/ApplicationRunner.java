package ru.otus.annotations;

import ru.otus.annotations.launcher.TestLauncher;
import ru.otus.annotations.test.TestWithExceptionInAfter;
import ru.otus.annotations.test.TestWithExceptionInBefore;
import ru.otus.annotations.test.TestWithExceptionInTest;

public class ApplicationRunner {

    public static void main(String[] args) {
        TestLauncher.launchTest(TestWithExceptionInBefore.class.getName());
        System.out.println("--------------------------------\n");
        System.out.println("--------------------------------\n");
        TestLauncher.launchTest(TestWithExceptionInTest.class.getName());
        System.out.println("--------------------------------\n");
        System.out.println("--------------------------------\n");
        TestLauncher.launchTest(TestWithExceptionInAfter.class.getName());
    }

}
