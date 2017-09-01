package com.vic.test.leetcode;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vic
 * Create time : 2017/8/30 下午12:31
 */
public class LeetcodeTest1_2 extends TestCase {

    /**
     * Given an array of integers, return indices of the two numbers such that they add up to a specific target.
     * You may assume that each input would have exactly one solution, and you may not use the same element twice.
     * Example:
     * Given nums = [2, 7, 11, 15], target = 9,
     * Because nums[0] + nums[1] = 2 + 7 = 9,
     * return [0, 1].
     */
    public void test1() {
        int[] nums = new int[]{2, 7, 11, 15};
        int target = 9;

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    System.out.println(nums[i] + " " + nums[j]);
                    break;
                }
            }
        }
    }

    /**
     * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.
     * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
     * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
     * Output: 7 -> 0 -> 8
     */
    public void test2() {
//        Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
//        Output: 7 -> 0 -> 8

        ListNode a1 = new ListNode(1);
//        ListNode a2 = new ListNode(4);
//        ListNode a3 = new ListNode(3);
//        a1.next = a2;
//        a2.next = a3;

        ListNode b1 = new ListNode(9);
        ListNode b2 = new ListNode(9);
//        ListNode b3 = new ListNode(4);
//        ListNode b4 = new ListNode(2);
        b1.next = b2;
//        b2.next = b3;
//        b3.next = b4;

        ListNode listNode = addTwoNumbers(a1, b1);
        System.out.println(listNode);
    }

    public ListNode addTwoNumbers(ListNode a1, ListNode b1) {
        if (null == a1.next && 0 == a1.val) {
            return b1;
        }
        if (null == b1.next && 0 == b1.val) {
            return a1;
        }
        List<ListNode> list = new ArrayList<>();
        ListNode aNext = a1;
        ListNode bNext = b1;
        do {
            int val = aNext.val + bNext.val;
            ListNode node = new ListNode(val);
            aNext = aNext.next;
            bNext = bNext.next;
            list.add(node);
        } while (null != aNext && null != bNext);

        while (null != aNext) {
            list.add(new ListNode(aNext.val));
            aNext = aNext.next;
        }
        while (null != bNext) {
            list.add(new ListNode(bNext.val));
            bNext = bNext.next;
        }
        List<ListNode> carry = carry(list, 0);

        ListNode listNode = parseListNode(carry);
        return listNode;
    }

    public List<ListNode> carry(List<ListNode> list, int index) {
        if (null == list) return null;

        if (index >= list.size()) {
            return list;
        }
        int val = list.get(index).val;
        if (val > 9) {
            list.get(index).val = val % 10;
            if (index + 1 < list.size()) {
                list.get(index + 1).val = list.get(index + 1).val + val / 10;
            } else {
                list.add(new ListNode(1));
            }
        }
        return carry(list, ++index);
    }

    public ListNode parseListNode(List<ListNode> nodeList) {
        if (null == nodeList && nodeList.size() == 0) {
            return null;
        }
        ListNode tmp = new ListNode(nodeList.get(0).val);
        ListNode root = tmp;
        for (int i = 1; i < nodeList.size(); i++) {
            ListNode node = new ListNode(nodeList.get(i).val);
            tmp.next = node;
            tmp = node;
        }
        return root;
    }


    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }


}
