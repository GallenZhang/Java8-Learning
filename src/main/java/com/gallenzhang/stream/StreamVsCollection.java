package com.gallenzhang.stream;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : zhangxq
 * @date : 2018/8/4
 * @description : 流 vs 集合
 */
public class StreamVsCollection {
    public static void main(String[] args) {
        List<Dish> vegetarianDished = Dish.menu.stream()
                .filter(Dish::isVegetarian)
                .collect(Collectors.toList());

        System.out.println(vegetarianDished);
    }
}
