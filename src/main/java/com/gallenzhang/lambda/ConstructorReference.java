package com.gallenzhang.lambda;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author : zhangxq
 * @date : 2018/8/3
 * @description : 构造函数引用
 */
public class ConstructorReference {
    public static void main(String[] args) {
        //无参构造
        Supplier<Apple> supplier = Apple :: new;  //() -> new Apple();
        Apple a1 = supplier.get();

        //一个参数
        Function<Integer,Apple> function = Apple :: new ; //(Integer weight) -> new Apple(weight)
        Apple a2 = function.apply(100);
        System.out.println(a2);

        //通过weight构造对象
        List<Integer> weights = Arrays.asList(7,3,4,10);
        List<Apple> apples = map(weights,Apple :: new);
        System.out.println(apples);

        //二个参数
        BiFunction<Integer,String,Apple> f = Apple :: new; //(Integer weight,String color) -> new Apple(weight,color)
        Apple a3 = f.apply(110,"green");
        System.out.println(a3);


        //三个参数
        TriFunction<String,String,Integer,Person> tf = Person :: new ;
        Person person = tf.apply("张三","360429199108052514",140);
        System.out.println(person);

    }

    public interface TriFunction<T,U,V,R>{
        R apply(T t, U u, V v);
    }

    public static List<Apple> map(List<Integer> list, Function<Integer,Apple> f){
        List<Apple> result = new ArrayList<>();
        for(Integer a : list){
            result.add(f.apply(a));
        }
        return result;
    }

    public static class Person{
        private String name;
        private String idNo;
        private Integer weight;

        public Person(String name, String idNo, Integer weight){
            this.name = name;
            this.idNo = idNo;
            this.weight = weight;
        }

        @Override
        public String toString(){
            return  "[name=" + name + " ,idNo=" + idNo + " ,weight=" + weight + "]";
        }
    }


    @Data
    public static class Apple{
        private Integer weight = 0;
        private String color = "";

        public Apple(){

        }

        public Apple(Integer weight){
            this.weight = weight;
        }

        public Apple(Integer weight, String color){
            this.weight = weight;
            this.color = color;
        }
    }
}
