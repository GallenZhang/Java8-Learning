package com.gallenzhang.collect;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.IntStream;

/**
 * @author : zhangxq
 * @date : 2018/8/6
 * @description :自定义收集器
 */
public class CustomCollector {
    public static void main(String[] args) {
        System.out.println(partitionPrimesWithCustomCollector(20));
    }


    public static class PrimeNumbersCollector implements Collector<Integer,Map<Boolean,List<Integer>>,Map<Boolean,List<Integer>>>{

        @Override
        public Supplier<Map<Boolean, List<Integer>>> supplier() {
            return () -> new HashMap<Boolean, List<Integer>>(){{
                put(true,new ArrayList<Integer>());
                put(false,new ArrayList<Integer>());
            }};
        }

        @Override
        public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
            return (map,value) -> map.get(isPrime(map.get(true),value)).add(value);
        }

        @Override
        public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
            return (map1,map2) -> {
                map1.get(true).addAll(map2.get(true));
                map1.get(false).addAll(map2.get(false));
                return map1;
            };
        }

        @Override
        public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
        }
    }

    /**
     * 是否是质数
     * @param primes
     * @param candidate
     * @return
     */
    public static boolean isPrime(List<Integer> primes,int candidate){
        int candidateRoot = (int) Math.sqrt(candidate);
        return takeWhile(primes,i -> i <= candidateRoot).stream()
                .noneMatch(p -> candidate % p ==0);

    }

    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p){
        int i=0;
        for(A a:list){
            if(!p.test(a)){
                return list.subList(0,i);
            }
            i++;
        }
        return list;
    }

    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
        return IntStream.rangeClosed(2, n).boxed()
                .collect(new PrimeNumbersCollector());
    }

}
