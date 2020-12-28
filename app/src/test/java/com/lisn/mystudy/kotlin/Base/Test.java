package com.lisn.mystudy.kotlin.Base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date : 2020/11/9 9:39 AM
 * @desc :
 */
class Test {

    class Person {

    }

    class Student extends Person {

    }

    Person person = new Person();

    Student student = new Student();

    void test() {
        // ? extends list 集合 能获取，不能修改
        ArrayList<? extends Person> list = new ArrayList<>();

        // 不能修改
//        list.add(person);
//        list.add(student);

        // 能获取
        Person person = list.get(0);
    }

    void show01(List<? extends Person> list) {
        // 能够获取
        for (Person person : list) {

        }

        // 不能修改
//        list.add(person);
    }

    void test01() {
        ArrayList<Student> list = new ArrayList<>();
        show01(list);
    }

    void show02(List<? super Student> list) {
        // 不能够获取
//        for (Student person : list) {
//
//        }

        // 能修改
        list.add(student);
    }

    void test02() {
        ArrayList<Person> list = new ArrayList<>();
        show02(list);
    }
}
