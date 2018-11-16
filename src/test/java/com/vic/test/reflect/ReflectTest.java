package com.vic.test.reflect;

import com.vic.test.spider.CtripHotelSpider;
import junit.framework.TestCase;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @Author: vic
 * @CreateTime : 2018/8/27 10:22
 */
public class ReflectTest extends TestCase {

    public void test1() {
        Class<CtripHotelSpider> clazz = CtripHotelSpider.class;

        boolean instance1 = clazz.isInstance(Integer.class);
        boolean instance = clazz.isInstance(CtripHotelSpider.class);
        System.out.println(instance1 + " " + instance);
    }

    /**
     * since 1.8s
     *
     * @param args
     */
    public static void main(String[] args) {
        // 1: SUM(n) = 1 + 2 + 3+ … + n-1 + n
        Function<Long, Long> fn1 = (n) -> Stream.iterate(1L, i -> ++i).limit(n).reduce((a, b) -> a + b).get();

        // 2.SUM1(n) = 1 + (1+2) + (1+ 2+ 3) + … (1 + 2 + 3 + … +n-1+n)
        Function<Long, Long> fn2 = (n) -> Stream.iterate(1L, i -> ++i).limit(n).reduce((a, b) -> a + fn1.apply(b)).get();

        // 3.FACT(n) = 1*2*3* . . . *(n-1) * n
        Function<Long, Long> fn3 = (n) -> Stream.iterate(1L, i -> ++i).limit(n).reduce((a, b) -> a * b).get();

        // 4.FACT_SUM(n) = 1 + 1*2 + 1*2*3 + … + 1*2*3* . . . *(n-1) * n
        Function<Long, Long> fn4 = (n) -> Stream.iterate(1L, i -> ++i).limit(n).reduce((a, b) -> a + fn3.apply(b)).get();

        long x = 100;
        System.out.println(String.format("fn1(%d)=%d", x, fn1.apply(x)));
        System.out.println(String.format("fn2(%d)=%d", x, fn2.apply(x)));
        System.out.println(String.format("fn3(%d)=%d", x, fn3.apply(x)));
        System.out.println(String.format("fn4(%d)=%d", x, fn4.apply(x)));
    }

}
