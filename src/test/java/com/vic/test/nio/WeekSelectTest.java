package com.vic.test.nio;

import com.vic.test.enums.WeekEnum;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by vic
 * Create time : 2018/6/22 13:58
 */
public class WeekSelectTest extends TestCase {

    public void test() {
        int select = WeekEnum.Sunday.code | WeekEnum.Saturday.code | WeekEnum.Monday.code;
        List<WeekEnum> selects = WeekEnum.getSelects(select);
        System.out.println(selects);
    }
}
