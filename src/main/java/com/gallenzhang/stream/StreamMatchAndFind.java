package com.gallenzhang.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author : zhangxq
 * @date : 2018/8/4
 * @description : 查找和匹配
 */
public class StreamMatchAndFind {
    public static void main(String[] args) {
        //anyMatch "流中是否有一个元素能匹配给定的谓词",是一个终端操作
        if(Dish.menu.stream().anyMatch(Dish::isVegetarian)){
            System.out.println("The menu is vegetarian friendly");
        }

        //allMath:"流中的元素是否都能匹配给定的谓词"
        boolean isHealthy = Dish.menu.stream().allMatch(d -> d.getCalories() < 1000);
        System.out.println("isHealthy:" + isHealthy);

        //noneMatch:和allMatch相反
        isHealthy = Dish.menu.stream().noneMatch(d -> d.getCalories() >= 1000);
        System.out.println("isHealthy:" + isHealthy);

        /**
         * anyMatch、allMatch、noneMatch都是终端操作
         * 三个操作用到了我们的短路操作，limit也是一个短路操作
         * 它只需要创建一个给定大小的流，而用不着处理流中所有的元素。
         * 碰到无限大小的流的时候，这种操作就有用了：可以把无限流变成有限流
         */


        //findAny方法返回当前流中的任意元素
        Dish.menu.stream().filter(Dish::isVegetarian).findAny().ifPresent(d -> System.out.println(d.getName()));

        //查找第一个元素,findFirst
        List<Integer> someNumbers = Arrays.asList(1,2,3,4,5);
        Optional<Integer> firstSquareDivisibleByThree = someNumbers.stream()
                .map(x -> x*x)
                .filter(x -> x % 3 == 0)
                .findFirst();
        System.out.println(firstSquareDivisibleByThree.get());

        /**
         * findFirst和findAny区别在于并行，findFirst在并行处理上限制更多
         */

    }
}
