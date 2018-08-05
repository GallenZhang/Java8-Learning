package com.gallenzhang.stream;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author : zhangxq
 * @date : 2018/8/5
 * @description :构建流
 */
public class BuildingStreams {
    public static void main(String[] args) {

        //1.由值创建流(使用Stream.of)
        Stream<String> stream = Stream.of("Java8","Lambdas","In","Action");
        stream.map(String::toUpperCase).forEach(System.out::println);
        Stream<String> emptyStream = Stream.empty();    //使用empty创建一个空流


        //2.由数组创建流
        int[] numbers = {2,3,5,7,11,13};
        int sum = Arrays.stream(numbers).sum();
        System.out.println("sum=" + sum);

        //3.由文件生成流
        long uniqueWords = 0;
        try(Stream<String> lines = Files.lines(Paths.get(BuildingStreams.class.getClassLoader().
                getResource("stream/data.txt").getPath()), Charset.defaultCharset())){
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
            System.out.println("uniqueWords:" + uniqueWords);
        }catch (IOException e){
            e.printStackTrace();
        }

        //4.由函数生成流：创建无限流，这里需要使用limit方式显示限制流的大小
        Stream.iterate(0,n -> n+2)
                .limit(10)
                .forEach(System.out::println);

        /**
         * 斐波纳契元组序列：0，1，1，2，3，5，8...,数列中开始两个数字是0和1，后续每个数字都是前两个数字之和
         * 斐波纳契元组序列与此类似，是数列中数字和后续数字组成的元组构成的序列：(0,1),(1,1),(1,2),(2,3),(3,5),(5,8)
         *
         * 用iterate方法生成斐波纳契元组序列中的前20个元素
         */
        Stream.iterate(new int[]{0,1},a -> new int[]{a[1],a[0]+a[1]})
                .limit(20)
                .forEach(a -> System.out.println(Arrays.toString(a)));

        Stream.iterate(new int[]{0,1},a -> new int[]{a[1],a[0]+a[1]})
                .limit(20)
                .map(a -> a[0])
                .forEach(a -> System.out.println(a));


        //5.生成：generate方法可以按需生成一个无限流，它不是依次对每个新生成的值应用函数，而是接受一个Supplier<T> 类型的Lambda提供新的值
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);


        /**
         * Math.random供应源是无状态的：它不会在任何地方记录任何值，以备以后计算使用
         * 但供应源不一定是无状态的，可以创建存储状态的供应源，它可以修改状态，并在为流生成下一个值时使用
         * 例子：我们可以用generate创建斐波纳契数列，来与iterate方法比较一下。
         * 注意：在并行代码中使用有状态的供应源是不安全的（下面的代码应该尽量避免使用）
         */
        IntStream ones = IntStream.generate(()->1);

        //匿名类与Lambda的区别：匿名类可以通过字段定义状态，而状态又可以用getAsInt方法来修改。这是一个副作用的例子
        //iterate的方法是纯粹不变的：它没有修改现在的状态，但在每次迭代时创建新的元组。（我们应该始终采用不变的方法，以便并行化处理，并保持结果正确）
        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;

            @Override
            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };
        IntStream.generate(fib).limit(10).forEach(System.out::println);


        /**
         * 需要注意的是：如果处理的是无限流，那么必须要用limit来显示限制流的大小，否则终端操作会永远计算下去。
         * 同样，不能对无限流做排序或者归约，因为所有元素都需要处理，而这永远也不可能完成。
         */


    }
}
