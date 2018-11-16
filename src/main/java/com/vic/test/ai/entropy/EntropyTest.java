package com.vic.test.ai.entropy;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 熵算法：集合纯度，数值越小越纯
 * @Author: vic
 * @CreateTime : 2018/8/29 15:22
 */
public class EntropyTest {

    public static void main(String[] args) {

        EntropyTest entropyTest = new EntropyTest();
        double entropy = entropyTest.entropy(Lists.newArrayList(1D, 2D));
        System.out.println(entropy);
    }

    public double log2N(double x) {
        return new BigDecimal(Math.log(x)).divide(new BigDecimal(Math.log(2)), 5, BigDecimal.ROUND_UP).doubleValue();
    }

    public double entropy(List<Double> numbers) {
        Map<Double, Long> numGroup = numbers.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        double result = 0;
        for (Double number : numbers) {
            Long count = numGroup.getOrDefault(number, 0L);
            double p = new BigDecimal(count).divide(new BigDecimal(numbers.size()), 5, BigDecimal.ROUND_UP).doubleValue();
            result -= p * log2N(p);
        }
        return result;
    }

}
