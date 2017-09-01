package com.vic.test.leetcode;

import junit.framework.TestCase;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Created by vic
 * Create time : 2017/8/31 下午9:23
 */
public class LeetcodeTest3 extends TestCase {

    /**
     * Given a string, find the length of the longest substring without repeating characters.
     * Examples:
     * Given "abcabcbb", the answer is "abc", which the length is 3.
     * Given "bbbbb", the answer is "b", with the length of 1.
     * Given "pwwkew", the answer is "wke", with the length of 3. Note that the answer must be a substring, "pwke" is a subsequence and not a substring.
     */
    public void test() {
        for (int i = 0; i < 1000; i++) {
            String s = RandomStringUtils.random(10, true, false).toLowerCase();
            System.out.println(s + " " + lengthOfLongestSubstring(s));
        }
    }

    public int lengthOfLongestSubstring(String s) {
        if (null == s || "".equals(s)) {
            return 0;
        }
        int max = 0;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            String c = s.charAt(i) + "";
            int index = sb.indexOf(c);
            if (index < 0) {
                sb.append(c);
                max = sb.length() > max ? sb.length() : max;
            } else {
                sb.append(c);
                int left = sb.substring(0, index + 1).length();
                int right = sb.substring(index + 1).length();
                max = left > right ? (left > max ? left : max) : (right > max ? right : max);
                sb = new StringBuilder(sb.substring(index + 1));
            }
            i++;
        }
        return max;
    }

}
