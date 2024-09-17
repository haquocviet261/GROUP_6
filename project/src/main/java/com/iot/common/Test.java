package com.iot.common;


import java.util.ArrayList;
import java.util.List;

public class Test {
    protected String name;
    Integer age;
    public Test() {
    }

    public Test(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Test(int age) {
        this.age = age;
    }
    public void test(int age){
       this.age =10;
    }

}
