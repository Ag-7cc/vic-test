package com.vic.test.java8;

import java.util.function.Function;

/**
 * Created by vic
 * Create time : 2017/7/28 下午2:09
 */
public class Lonmda {


    public static void main(String[] args) {
        Function<Integer, String> function = str -> "{" + str + "}";
        String apply = function.apply(6);
        System.out.println(apply);
    }
}
