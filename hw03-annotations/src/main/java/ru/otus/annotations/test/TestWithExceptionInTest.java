package ru.otus.annotations.test;

import ru.otus.annotations.annotation.After;
import ru.otus.annotations.annotation.Before;
import ru.otus.annotations.annotation.Test;

public class TestWithExceptionInTest {

    @Before
    public void before() {
        System.out.println("before");
    }

    @After
    public void after() {
        System.out.println("before");
    }

    @Test(name = "first test")
    public void firstTest() {
        System.out.println("first test");
        throw new RuntimeException("exception");
    }

    @Test(name = "second test")
    public void secondTest() {
        System.out.println("second test");
    }

}
