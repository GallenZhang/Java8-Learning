package com.gallenzhang.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author : zhangxq
 * @date : 2018/8/3
 * @description : 函数式接口
 */
public class FunctionInterface {

    public static void main(String[] args) {
        List<String> inventory = Arrays.asList("green","green","red","blue","white");
        List<String> filterList = filter(inventory,(String a) -> "green".equals(a));
        System.out.println(inventory);
        System.out.println(filterList);
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p){
        List<T> result = new ArrayList<>();
        for(T t: list){
            if(p.test(t)){
                result.add(t);
            }
        }
        return result;
    }
}
