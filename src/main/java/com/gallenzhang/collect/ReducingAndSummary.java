package com.gallenzhang.collect;

import com.gallenzhang.stream.Dish;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Optional;

import static java.util.stream.Collectors.*;


/**
 * @author : zhangxq
 * @date : 2018/8/5
 * @description :归约和汇总
 */
public class ReducingAndSummary {

    public static void main(String[] args) {
        //1.counting
        Dish.menu.stream().collect(counting()); //Dish.menu.stream().count()

        //2.max、min
        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish = Dish.menu.stream().collect(maxBy(dishCaloriesComparator));//Optional可以包含也可以不包含值

        //3.sum
        int totalCalories = Dish.menu.stream().collect(summingInt(Dish::getCalories));

        //4.average
        double avgCalories = Dish.menu.stream().collect(averagingDouble(Dish::getCalories));

        //5.IntSummaryStatistics
        IntSummaryStatistics menuStatistics = Dish.menu.stream().collect(summarizingInt(Dish::getCalories));

        //6.joining
        String shortMenu = Dish.menu.stream().map(Dish::getName).collect(joining(","));
        System.out.println("shortMenu:" + shortMenu);

        //7.广义的归约汇总:collect方法特别适合表达可变容器的归约
        mostCalorieDish = Dish.menu.stream()
                .collect(reducing((d1,d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));

        //8.收集框架的灵活性
        totalCalories = Dish.menu.stream().collect(reducing(0, //初始值
                Dish::getCalories,                                    //转换函数
                Integer::sum));                                       //累积函数
        totalCalories = Dish.menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
        totalCalories = Dish.menu.stream().mapToInt(Dish::getCalories).sum();   //  最佳方案


        /**
         * collect实际上是一个归约操作,collect是一个终端操作，它接受的参数是将流中元素累积到汇总结果的各种方式(称为收集器)
         *
         * 函数式编程通常提供了许多方法执行同一操作。
         * 收集器在某种程度上来说比Stream接口提供的方法用起来更复杂，但好处是能提供更高水平的抽象和概括，更容易重用和自定义。
         *
         * 建议：尽可能为手头上的问题探索不同的解决方案，但在通用的方案里，始终选择最专门化的一个。无论从可读性还是性能上看，一般都是最好的决定。
         *
         * 要统计菜单的总热量，倾向于选择IntStream，因为它最简明、也最易读，同时是性能最好的一个（避免了自动拆箱操作）
         */
    }
}
