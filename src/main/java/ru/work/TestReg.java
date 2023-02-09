package ru.work;

import java.util.*;

public class TestReg {
    public static void main(String[] args) {
        Person person = new Person("MAY", 11);
        Person person1 = new Person("MAY",20);

        Map<Person, Integer> personMap = new HashMap<>();
        personMap.put(person, personMap.getOrDefault(person, 0) + 5000);

        System.out.println(personMap.get("MAY"));

    }
}

class Person {
    String name = "";
    int age = 0;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}