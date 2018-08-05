package com.gallenzhang.stream;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author : zhangxq
 * @date : 2018/8/5
 * @description : 原始类型流特化、数值范围、数值流应用
 */
public class NumericStreams {

    public static void main(String[] args) {

        //代码中暗含装箱成本，每个Integer都必须拆箱成一个原始类型再求和。
        int calories = Dish.menu.stream()
                .map(Dish::getCalories)
                .reduce(0,Integer::sum);

        /**
         * Java8引入了三个原始类型特化流接口，IntStream、DoubleStream、LongStream，
         * 分别将流中元素转化为int、long、double,从而避免了暗含的装箱成本
         *
         * 每个接口都带来了进行常用数值归约的新方法，比如：sum、max、min、average
         * 此外，在必要的时候还可以再把他们转换回对象流的方法
         *
         */

        //映射到数值流
        calories = Dish.menu.stream()
                .mapToInt(Dish::getCalories)   //这里返回一个IntStream,而不是Stream<Integer>
                .sum();


        //转回对象流
        IntStream intStream = Dish.menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> stream = intStream.boxed();

        //默认值OptionalInt
        /**
         * 如果要计算IntStream中最大的元素，就不能用上面的方法了，因为上面求和有一个默认值0
         * 这里需要区分没有元素的流和最大值真的为0的流
         *
         * Optional,这是一个可以表示值存在或者不存在的容器
         * 对于三种原始流特化，分别有一个Optional原始类型特化版本：OptionalInt、OptionalDouble、OptionalLong
         */
        OptionalInt maxCalories = Dish.menu.stream()
                .mapToInt(Dish::getCalories)
                .max();
        int max = maxCalories.orElse(1);   //如果没有最大值的话，显示提供一个默认最大值


        /**
         * 数值范围：IntStream、LongStream提供两个静态方法帮助生成这种范围数值：range、rangeClosed(包含结束值)
         */
        IntStream evenNumbers = IntStream.rangeClosed(1,100).filter(d -> d % 2 == 0); //1-100的偶数流
        System.out.println(evenNumbers.count());


        /**
         * 数值应用：勾股数
         *
         * 勾股定理：a*a + b*b = c*c
         *
         * 要点1：如何表示三个数？ 可以定义一个新的类来表示三元数，这里用具有三个元素的数组，如 new int[]{3,4,5} 来表示勾股数(3,4,5)
         *
         * 要点2：如何确定是否勾股数？ 假设a、b已经确定，需要测试 a*a + b*b 是不是整数，（整数的判断方式:value % 1 == 0）
         * Math.sqrt(a*a + b*b) % 1 == 0
         *
         * 要点3：如何生成a、b的值？ 利用IntStream的rangeClosed即可
         * IntStream.rangeClosed(1,100)
         */

        //这里需要用mapToOj,因为IntStream中的map方法只能为流中的每个元素返回另一个int,但可以用mapToObj方法改写它，返回一个对象值流
        Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1,100)
                .boxed()
                .flatMap(  //将三元数流扁平化，Stream<Stream<int[]>>  ---> Stream<int[]>
                         a -> IntStream.rangeClosed(a,100)
                                .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
                                .mapToObj(b -> new int[]{a,b,(int)Math.sqrt(a*a + b*b)}));

        pythagoreanTriples.limit(5).forEach(a -> System.out.println(Arrays.toString(a)));

        //优化：目前需要求两次平方根，可以先生成所有的三元数，然后再筛选符合条件的
        Stream<double[]> pythagoreanTriples2 = IntStream.rangeClosed(1,100)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(a,100)
                    .mapToObj(b -> new double[]{a,b,Math.sqrt(a*a + b*b)}))
                    .filter(a -> a[2] % 1 == 0);
        pythagoreanTriples2.limit(5).forEach(b -> System.out.println(Arrays.toString(b)));

    }
}
