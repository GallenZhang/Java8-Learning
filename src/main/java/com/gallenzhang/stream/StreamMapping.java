package com.gallenzhang.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : zhangxq
 * @date : 2018/8/4
 * @description : 映射
 */
public class StreamMapping {

    public static void main(String[] args) {
        //取出每道菜名长度
        List<Integer> dishNameLengths = Dish.menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println(dishNameLengths);

        //遍历List<String[]>
        List<String> words = Arrays.asList("Hello","World");
        words.stream().map(word -> word.split("")).distinct().forEach(
                (String[] a) -> Arrays.asList(a).stream().forEach(str -> System.out.print(str + " ")));

        System.out.println("\r\n----------------Arrays.stream-----------------");
        //Arrays.stream
        String[] arrayOfWords = {"Goodbye","World"};
        Stream<String> streamOfWords = Arrays.stream(arrayOfWords);
        streamOfWords.distinct().forEach(System.out::println);


        //第一个map将string转换为String[]，Arrays::stream让每个数组变成一个单独的流
        words.stream().map(word -> word.split("")).map(Arrays::stream).distinct().collect(Collectors.toList());

        System.out.println("\r\n-------------flatMap------------------");
        /**
         * 流的扁平化 flatMap
         * flatMap将各个生成流扁平化为单个流
         */
        words.stream().map(word -> word.split("")).flatMap(Arrays::stream).
                collect(Collectors.toList()).forEach(System.out::print);


        System.out.println("\r\n---------------练习题----------------");
        //1.给定[1,2,3,4,5] ,返回[1,4,9,16,25]
        List<Integer> numbers = Arrays.asList(1,2,3,4,5);
        numbers.stream().map(a -> a*a).collect(Collectors.toList()).forEach(System.out::println);

        /**
         * 2.给定两个数字列表，如何返回所有的数对呢?
         * 例如，给定列表[1, 2, 3]和列表[3, 4]
         * 应该返回[(1, 3), (1, 4), (2, 3), (2, 4), (3, 3), (3, 4)]。
         * 为简单起见,你可以用有两个元素的数组来代 表数对。
         */
        List<Integer> number1 = Arrays.asList(1,2,3);
        List<Integer> number2 = Arrays.asList(3,4);
        List<int[]> pairs = number1.stream().flatMap(i -> number2.stream().map(j -> new int[]{i,j})).collect(Collectors.toList());
        pairs.stream().forEach(a -> System.out.println(Arrays.toString(a)));

        /**
         * 3.如何扩展前一个例子，只返回总和能被3整除的数对呢?
         *  例如(2, 4)和(3, 3)是可以的。
         */
        System.out.println();
        pairs = number1.stream().flatMap(i -> number2.stream().filter(j -> (i+j) % 3 == 0 ).map(j -> new int[]{i,j})).collect(Collectors.toList());
        pairs.stream().forEach(a -> System.out.println(Arrays.toString(a)));

    }
}
