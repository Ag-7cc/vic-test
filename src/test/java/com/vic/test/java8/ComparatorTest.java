package com.vic.test.java8;

import com.google.common.collect.Lists;
import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by vic
 * Create time : 2017/4/28 下午6:11
 */
public class ComparatorTest extends TestCase {


    public List<ComparatorTest.User> getData() {

        List<ComparatorTest.User> list = Lists.newArrayList();
        list.add(new ComparatorTest.User("张", 3));
        list.add(new ComparatorTest.User("张", 1));
        list.add(new ComparatorTest.User("张", 2));
        list.add(new ComparatorTest.User("张", 4));
        list.add(new ComparatorTest.User("李", 1));
        list.add(new ComparatorTest.User("李", 2));
        return list;
    }

    public void test1() {

        List<ComparatorTest.User> data = getData();

//        data.sort(Comparator.<ComparatorTest.User, String>comparing(item -> item.firstName)
//                .thenComparing(Function.identity(), (next, cur) ->
//                cur.age.compareTo(next.age)
//        ));

        data.sort(/*名称生序*/
                Comparator.comparing((ComparatorTest.User item) -> item.firstName)
                        /*年龄倒叙*/
                        .thenComparing(Function.identity(), (next, cur) -> cur.age.compareTo(next.age)));

        data.forEach(System.err::println);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String firstName;
        private Integer age;
    }
}
