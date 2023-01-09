package ru.otus;

import com.google.common.collect.Ordering;

import java.util.Arrays;
import java.util.List;

public class HelloOtus {
    public static void main(String[] args) {

        List<Integer> integers = Arrays.asList(1125, 23, 57, 21, 678, null, 12, 12344);
        integers.sort(Ordering.natural().nullsFirst());
        System.out.println(integers);
    }
}