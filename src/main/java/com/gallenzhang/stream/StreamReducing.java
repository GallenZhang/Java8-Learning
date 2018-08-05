package com.gallenzhang.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author : zhangxq
 * @date : 2018/8/4
 * @description : 归约
 */
public class StreamReducing {

    public static void main(String[] args) {
        //reduce 将一个数字列表归约成一个数字
        List<Integer> numbers = Arrays.asList(4,5,3,9);
        System.out.println(numbers.stream().reduce(0,(a,b) -> a+b));  //reduce(9,Integer::sum)

        //无初始值的reduce
        Optional<Integer> num = numbers.stream().reduce(Integer::sum);

        //使用归约求数字列表最大值和最小值
        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        Optional<Integer> min = numbers.stream().reduce(Integer::min);

        //使用map 和 reduce组合，统计有多少个菜？
        Dish.menu.stream().map(d -> 1).reduce((a,b) -> a + b).ifPresent(System.out::println);
        System.out.println(Dish.menu.stream().count());


        /**
         * map和reduce连接通常称为map-reduce模式，因Google用它进行网络搜索出名，因为它很容易并行化
         *
         * int sum = 0;
         * for(int x : numbers){
         *     sum += x;
         * }
         * 外部迭代不容易并行化处理，如果加入同步，那么线程竞争会抵消并行本应带来的性能提升。
         *
         *
         * 归约方法优势与并行化：
         * 相比于普通迭代求和(外部迭代)，使用内部迭代的reduce得以选择并行执行reduce操作。(这里的迭代被内部迭代抽象掉了)
         * 并行操作的思路：将输入分块，分块求和，最后再合并起来。（fork/join框架）
         *
         * 注意：可变的累加模式对于并行化来说是死路一条，使用流对所有元素并行求和，代码几乎不用改动：将stream()换成parallelStream()
         *
         * reduce并行代价：传递给reduce的Lambda不能更改状态(实例变量)，而且操作必须满足结合律才可以按任意顺序执行。
         *
         */
    }
}
