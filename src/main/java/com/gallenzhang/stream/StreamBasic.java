package com.gallenzhang.stream;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : zhangxq
 * @date : 2018/8/3
 * @description :
 */
public class StreamBasic {
    public static void main(String[] args) {
        List<String> lowCaloricDishesName =
                Dish.menu.parallelStream().filter(d -> d.getCalories() < 400)
                        .sorted(Comparator.comparing(Dish::getCalories))
                        .map(Dish::getName)
                        .collect(Collectors.toList());

        System.out.println(lowCaloricDishesName);

        List<String> threeHighCaloricDishNames =
                Dish.menu.stream().filter(d -> d.getCalories() > 300)
                        .map(Dish::getName)
                        .limit(3)
                        .collect(Collectors.toList());
        System.out.println(threeHighCaloricDishNames);


    }
}
