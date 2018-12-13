package com.vic.test;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: vic
 * @CreateTime : 2018/11/16 15:35
 */
public class DemoTest extends TestCase {

    public void test1() {
        String[] arr = new String[]{"a", "b", "c", "d", "e", "f"};
        for (int i = 0; i < (arr.length / 2); i++) {
            String tmp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = tmp;
        }
        System.out.println(Arrays.toString(arr));
    }

    public void test2() {
        for (int i = 0; i < 100; i++) {
            if (isPrimeNumber(i)) {
                System.out.println(i + "\t");
            }
        }
    }

    /**
     * 素数大于等于2 不能被它本身和1以外的数整除
     * 使用Math.sqrt是有理论根据的：对正整数n，如果用2到根号n之间的所有整数去除，均无法整除，则n为素数。所以这么写能减少循环次数，提高效率。
     *
     * @param num
     * @return
     */
    public static boolean isPrimeNumber(int num) {
        if (num <= 3) {
            // 小于等于3的数中，只有2和3是素数，所以就不放循环判断
            return num > 1;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取下一天日期
     *
     * @return
     */
    public static Date getNextDay() {
        try {
            Thread.sleep(24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public void test3(){
        Integer a = 220;
        inc(a);
        System.out.println(a);
        String s = "1";
        inc2(s);
        System.out.println(s);

        AtomicInteger atomicInteger = new AtomicInteger(0);
        inc3(atomicInteger);
        System.out.println(atomicInteger.get());
    }

    public void test4() {
        String str = "哈哈哈href=\"https://www.phimall.com/m/notice-detail-64.html\"我想要引号里面的值";
        Pattern pattern = Pattern.compile("href\\s*=\\s*\"[^\"]*\"");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
//            System.out.println(matcher.group(1));
        }
    }


    public void test5() {
        String s = "[2018-12-11T15:12:42.393Z] [INFO] end clone repo http:[INFO]10.64.21.140/CIDemo/config-center.git\n" +
                "[2018-12-11T15:12:43.394Z] [INFO] start exec command export MAC=64 && cd CIDemo/config-center && sh build.sh\n" +
                "[2018-12-11T15:12:44.724Z] [INFO]\n";
        String s1 = timeReplace(s);
        System.out.println(s1);
    }

    public String timeReplace(String source) {
        final Pattern timePattern = Pattern.compile("\\[(20\\d{2}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[A-Z])]");
        final int pos = 1;
        Matcher matcher = timePattern.matcher(source);
        matcher.reset();
        boolean result = matcher.find();
        if (result) {
            StringBuffer sb = new StringBuffer();
            do {
                String replacement = String.format("[<span class=\"time\">%s</span>]", matcher.group(pos));
                matcher.appendReplacement(sb, replacement);
                result = matcher.find();
            } while (result);
            matcher.appendTail(sb);
            return sb.toString();
        }
        return source;
    }


    private void inc(Integer i){
        i++;
    }
    private void inc2(String s){
        s +="2";
    }

    public void inc3(AtomicInteger a){
        a.incrementAndGet();
    }
}
