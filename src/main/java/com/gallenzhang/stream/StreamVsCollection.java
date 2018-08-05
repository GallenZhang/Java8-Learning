package com.gallenzhang.stream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : zhangxq
 * @date : 2018/8/4
 * @description : 流 vs 集合
 */
public class StreamVsCollection {
    public static void main(String[] args) {

        //流式过滤
        List<Dish> vegetarianDished = Dish.menu.stream()
                .filter(Dish::isVegetarian)
                .collect(Collectors.toList());

        //collection过滤
        List<Dish> filterList = new ArrayList<>();
        for(Dish dish:Dish.menu){
            if(dish.isVegetarian()){
                filterList.add(dish);
            }
        }
        System.out.println(filterList);
        System.out.println(vegetarianDished);

        System.out.println("-------------------");

        //distinct
        List<Integer> numers = Arrays.asList(1,2,1,3,3,2,4);
        numers.stream().distinct().forEach(System.out::println);

        //collection去重
        Set<Integer> numerSet = new HashSet<>();
        for(Integer i : numers){
            numerSet.add(i);
        }
        numerSet.stream().forEach(System.out::println);

        System.out.println("-------------------");

        //limit
        List<Dish> dishes = Dish.menu.stream()
                .filter(d -> d.getCalories() > 300)
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(dishes);

        //skip
        dishes = Dish.menu.stream()
                .filter(d -> d.getCalories() > 300)
                .skip(2)
                .collect(Collectors.toList());
        System.out.println(dishes);
    }
}
