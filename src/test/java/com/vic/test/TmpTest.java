package com.vic.test;

import com.google.common.collect.Lists;
import junit.framework.TestCase;

import java.util.List;
import java.util.Objects;

/**
 * Created by vic
 * Create time : 2018/7/4 14:35
 */
public class TmpTest extends TestCase {
    /**
     * 一个城市的名称经过旋转后可能正好是另一个城市的名称。将所有匹配的城市名称放在同一组。
     * 只能旋转。第一个字符移动到最后一个。每次只能移动一个。
     */
    public void test1() {
        String[] arr = new String[]{"Tokyo", "London", "Rome", "Donlon", "Kyoto", "Paris"};
        List<Integer> skipIndex = Lists.newArrayList();
        List<List<String>> targetList = Lists.newArrayList();
        for (int i = 0; i < arr.length; i++) {
            if (skipIndex.contains(i)) {
                continue;
            }
            List<String> target = Lists.newArrayList();
            target.add(arr[i]);

            List<String> strings = getRotateList(arr[i]);
            for (int j = 0; j < arr.length; j++) {
                if (i == j) {
                    continue;
                }
                if (strings.contains(arr[j].toLowerCase())) {
                    target.add(arr[j]);
                    skipIndex.add(j);
                }
            }
            targetList.add(target);
        }
        System.out.println(targetList);
    }

    private List<String> getRotateList(String str) {
        List<String> strList = Lists.newArrayList();
        String rotate = str;
        for (int i = 0; i < str.length(); i++) {
            rotate = rotate(rotate);
            strList.add(rotate.toLowerCase());
        }
        return strList;
    }

    private String rotate(String str) {
        if (Objects.isNull(str)) {
            return null;
        }
        if (Objects.nonNull(str) && str.length() == 1) {
            return str;
        }
        return str.substring(1) + str.substring(0, 1);
    }
}
