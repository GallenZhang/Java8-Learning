package com.gallenzhang.collect;
import com.gallenzhang.stream.Dish;

import java.util.*;

import static com.gallenzhang.stream.Dish.*;
import static java.util.stream.Collectors.*;

/**
 * @author : zhangxq
 * @date : 2018/8/5
 * @description : 分组
 */
public class Grouping {

    public static void main(String[] args) {
        //group
        Map<Dish.Type,List<Dish>> dishesByType= menu.stream().collect(groupingBy(Dish::getType));
        System.out.println(dishesByType);

        //自定义分类函数（自定义Function(K,R)）
        Map<CaloricLevel,List<Dish>> dishesByCaloricLevel = menu.stream()
                .collect(groupingBy(d -> {
                    if(d.getCalories() <= 400){
                        return CaloricLevel.DIET;
                    }else if(d.getCalories() <= 700){
                        return CaloricLevel.NORMAL;
                    }else {
                        return CaloricLevel.FAT;
                    }
                }));
        System.out.println(dishesByCaloricLevel);

        //多级分组（双参数版本的Collectors.groupingBy）
        Map<Dish.Type,Map<CaloricLevel,List<Dish>>> dishesByTypeCaloricLevel = menu.stream()
                .collect(groupingBy(Dish::getType,
                        groupingBy(d-> {
                            if(d.getCalories() <= 400) return CaloricLevel.DIET;
                            else if(d.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        })));
        System.out.println(dishesByTypeCaloricLevel);

        //按子组收集数据
        //普通单参数groupingBy(f)其中f是分类函数，实际上是groupingBy(f,toList())的简便写法
        Map<Dish.Type,Long> typesCount = menu.stream().collect(groupingBy(Dish::getType,counting()));
        System.out.println(typesCount);

        //按照菜的类型查找菜单中热量最高的菜
        Map<Dish.Type,Optional<Dish>> mostCaloricByType = menu.stream().
                collect(groupingBy(Dish::getType,maxBy(Comparator.comparingInt(Dish::getCalories))));
        System.out.println(mostCaloricByType);

        //将收集器的结果转换为另一种类型
        Map<Dish.Type,Dish> mostCaloric = menu.stream().
                collect(groupingBy(Dish::getType,
                        collectingAndThen(
                                maxBy(Comparator.comparingInt(Dish::getCalories)),t -> t.get())
                        ));
        System.out.println(mostCaloric);

        //与groupingBy联合使用的其他收集器的例子
        Map<Dish.Type,Integer> totalCaloriesByType = menu.stream()
                .collect(groupingBy(Dish::getType,summingInt(Dish::getCalories)));
        System.out.println(totalCaloriesByType);

        Map<Dish.Type,Set<CaloricLevel>> caloricLeavelByType = menu.stream()
                .collect(groupingBy(Dish::getType,mapping(
                        d -> {
                            if(d.getCalories() <= 400){
                                return CaloricLevel.DIET;
                            }else if(d.getCalories() <= 700)return CaloricLevel.NORMAL;
                            else{
                                return CaloricLevel.FAT;
                            }
                        } ,toSet()
                )));
        System.out.println(caloricLeavelByType);

        //使用toCollection确定返回类型
        caloricLeavelByType = menu.stream()
                .collect(groupingBy(Dish::getType,mapping(
                        d -> {
                            if(d.getCalories() <= 400)return CaloricLevel.DIET;
                            else if(d.getCalories() <= 700)return CaloricLevel.NORMAL;
                            else{
                                return CaloricLevel.FAT;
                            }
                        },toCollection(HashSet::new)
                )));
        System.out.println(caloricLeavelByType);


    }

    public enum CaloricLevel { DIET, NORMAL, FAT }
}
