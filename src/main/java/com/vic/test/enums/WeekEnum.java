package com.vic.test.enums;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vic
 * Create time : 2018/6/22 17:05
 */
public enum WeekEnum {
    Sunday(1 << 0, "周日"),
    Monday(1 << 1, "周一"),
    Tuesday(1 << 2, "周二"),
    Wednesday(1 << 3, "周三"),
    Thursday(1 << 4, "周四"),
    Friday(1 << 5, "周五"),
    Saturday(1 << 6, "周六");
    public int code;
    public String desc;

    WeekEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<WeekEnum> getSelects(int selectCode) {
        List<WeekEnum> selects = new ArrayList<>();
        for (WeekEnum weekEnum : WeekEnum.values()) {
            System.out.println(prettyPrint(selectCode) + "\t" + prettyPrint(weekEnum.code) + "\t" + prettyPrint(selectCode & weekEnum.code));
            if ((selectCode & weekEnum.code) != 0) {
                selects.add(weekEnum);
            }
        }
        return selects;
    }

    @Override
    public String toString() {
        return this.desc;
    }

    public static String prettyPrint(int code) {
        return StringUtils.leftPad(Integer.toBinaryString(code), 7, "0");
    }
}
