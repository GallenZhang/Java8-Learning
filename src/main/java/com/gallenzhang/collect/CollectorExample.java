package com.gallenzhang.collect;

import com.gallenzhang.stream.Dish;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author : zhangxq
 * @date : 2018/8/6
 * @description : 收集器示例
 */
public class CollectorExample {
    public static void main(String[] args) {

        /**
         * T :是流中要收集的项目的泛型
         *
         * A :是累加器的类型，累加器是在收集过程中用于累积部分结果的对象
         *
         * R :是收集操作得到的对象(通常但并不一定是集合)的类型
         *
         * public interface Collector<T, A, R> {
         *
         *     //建立新的结果容器
         *     Supplier<A> supplier();
         *
         *     //将元素添加至结果容器
         *     BiConsumer<A, T> accumulator();
         *
         *     //合并两个结果容器
         *     BinaryOperator<A> combiner();
         *
         *     //将结果容器应用最终转换
         *     Function<A, R> finisher();
         *
         *     //返回一个不可变的Characteristics集合，它定义了收集器的行为
         *     UNORDERED: 归约结果不受流中项目的遍历和累积顺序的影响。
         *     CONCURRENT: accumulator函数可以从多个线程同时调用，且该收集器可以并行归约流。如果收集器没有标为UNORDERED，那它仅在用于无序数据源时才可以并行归约
         *     IDENTITY_FINISH: 这表明完成器方法返回的函数是一个恒等函数，可以跳过。这也意味着，将累加器A不加检查地转换为结果R是安全的。
         *     Set<Characteristics> characteristics();
         * }
         *
         *
         */
        List<Dish> dishes = Dish.menu.stream().collect(new ToListCollector<>());
        System.out.println(dishes);

        //进行自定义收集而不去实现Collector,Stream有一个重载的collect方法可以接受另外三个函数-supplier、accumulator和combiner
        dishes = Dish.menu.stream().collect(ArrayList::new,List::add,List::addAll);
        System.out.println(dishes);

    }

    public static class ToListCollector<T> implements Collector<T,List<T>,List<T>>{

        @Override
        public Supplier<List<T>> supplier() {
            return ArrayList::new; //() -> new ArrayList<T>();
        }

        @Override
        public BiConsumer<List<T>, T> accumulator() {
            return (List<T> list,T item) -> list.add(item);  //List::add
        }

        @Override
        public BinaryOperator<List<T>> combiner() {
            return (list1,list2) -> {
                    list1.addAll(list2);
                    return list1;
                };
        }

        @Override
        public Function<List<T>, List<T>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH,
                    Collector.Characteristics.CONCURRENT));
        }
    }
}
