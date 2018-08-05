package com.gallenzhang.collect;

import com.gallenzhang.stream.Dish;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.gallenzhang.stream.Dish.menu;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

/**
 * @author : zhangxq
 * @date : 2018/8/5
 * @description :分区
 */
public class Partitioning {
    public static void main(String[] args) {
        //分区是分组的特殊情况，由一个谓词作为分类函数，分区函数返回一个布尔值，意味着分组Map的键类型是Boolean
        Map<Boolean,List<Dish>> partitionedMenu = menu.stream()
                .collect(groupingBy(Dish::isVegetarian));
        System.out.println(partitionedMenu);

        //使用filter筛选
        List<Dish> vegetarianDishes = menu.stream().filter(Dish::isVegetarian).collect(toList());
        System.out.println(vegetarianDishes);

        //partitioningBy的重载版本
        Map<Boolean,Map<Dish.Type,List<Dish>>> vegetarianDishesByType = menu.stream().
                collect(partitioningBy(Dish::isVegetarian,groupingBy(Dish::getType)));
        System.out.println(vegetarianDishesByType);

        //查找素食和非素食中热量最高的菜
        Map<Boolean,Dish> mostCaloricByVegetarian = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian,
                        collectingAndThen(
                                maxBy(comparingInt(Dish::getCalories)),t -> t.get())
                ));
        System.out.println(mostCaloricByVegetarian);


        //将数字按照质数和非质数分区
        System.out.println(partitionPrimes(20));


    }

    /**
     * 区分是否是质数
     * @param candidate
     * @return
     */
    public static boolean isPrime(int candidate){
        int candidateRoot = (int) Math.sqrt(candidate);
        return IntStream.rangeClosed(2,candidateRoot).noneMatch(i -> candidate % i ==0);
    }

    public static Map<Boolean,List<Integer>> partitionPrimes(int n){
        return IntStream.rangeClosed(2,n).boxed()
                .collect(partitioningBy(i -> isPrime(i)));
    }
}
