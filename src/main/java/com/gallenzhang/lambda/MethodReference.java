package com.gallenzhang.lambda;

import lombok.Data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author : zhangxq
 * @date : 2018/8/3
 * @description : 方法引用
 */
public class MethodReference {
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple(80,"green"),new Apple(155,"green"),new Apple(120,"red"));

        inventory.sort((Apple a1,Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
        System.out.println(inventory);

        //方法引用
        inventory.sort(Comparator.comparing(Apple::getWeight));
        System.out.println(inventory);
    }

    @Data
    public static class Apple{
        private Integer weight = 0;
        private String color = "";

        public Apple(Integer weight, String color){
            this.weight = weight;
            this.color = color;
        }
    }
}
