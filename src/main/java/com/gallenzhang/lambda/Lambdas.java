package com.gallenzhang.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author : zhangxq
 * @date : 2018/8/3
 * @description : lambda(匿名函数)
 */
public class Lambdas {

    public static void main(String[] args) {
        //simple example
        Runnable r = () -> System.out.println("Hello");
        r.run();

        List<Apple> inventory = Arrays.asList(new Apple(80,"green"),new Apple(155,"green"),new Apple(120,"red"));

        //filtering with lambdas
        List<Apple> greeApples = filter(inventory, (Apple a) -> "green".equals(a.getColor()));
        System.out.println(greeApples);
    }

    public static class AppleComparator implements Comparator<Apple>{
        @Override
        public int compare(Apple a1, Apple a2) {
            return a1.getWeight().compareTo(a2.getWeight());
        }
    }

    interface ApplePredicate{
        boolean test(Apple a);
    }

    public static List<Apple> filter(List<Apple> inventory,ApplePredicate p){
        List<Apple> result = new ArrayList<>();
        for(Apple apple : inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return  result;
    }

    public static class Apple{
        private Integer weight = 0;
        private String color = "";

        public Apple(Integer weight, String color){
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String toString() {
            return "Apple{" +
                    "color='" + color + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }
}
