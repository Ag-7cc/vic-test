package com.vic.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple BootApplication.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void test() {

        for (int x = 200; x <= 200; x += 10) {
            for (int y = 1000; y < 20000; y++) {
                double price1 = 5 * x + 0.06 * y;
                double price2 = 0.5 * x + 0.11 * y;
                double diff = Math.abs(price1 - price2);
                System.out.println(String.format("隐私号个数：%s \t 通话时长:%s \t AXB价格：%s \t AXN价格：%s \t 差价:%s", x, y, Math.round(price1), Math.round(price2), Math.round(price1 - price2)));
//                if (diff <= 0) {
//                    System.out.println(String.format("隐私号个数：%s \t 通话时长:%s \t AXB价格：%s \t AXN价格：%s \t", x, y, Math.round(price1), Math.round(price2)));
//                    break;
//                }
            }

        }


    }
}
