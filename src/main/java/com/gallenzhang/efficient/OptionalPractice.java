package com.gallenzhang.efficient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * @author : zhangxq
 * @date : 2018/8/16
 * @description :Optional实战示例
 */
public class OptionalPractice {
    public static void main(String[] args) {
        //1.用Optional封装可能为null的值
        Map<String,Object> map = new HashMap<>();
        Optional<Object> value = Optional.ofNullable(map.get("key"));

        //2.异常与Optional的对比
        Optional<Integer> integerValue  = stringToInt("20");

        /**
         * 虽然Optional也有基础类型：OptionalInt、OptionalLong、OptionalDouble，但不推荐使用基础类型的Optional，因为
         * 基础类型的Optional不支持map、flatMap以及filter方法，而这些却是Optional类最有用的方法
         */

        //3.把所有内容整合起来
        Properties properties = new Properties();
        properties.setProperty("a","5");
        properties.setProperty("b","true");
        properties.setProperty("c","-3");
        System.out.println(readDurationByOptional(properties,"a"));
        System.out.println(readDurationByOptional(properties,"b"));
        System.out.println(readDurationByOptional(properties,"c"));
        System.out.println(readDurationByOptional(properties,"d"));


        /**
         * 使用Optional和Stream的通用模式：都是对数据库查询过程的反思，查询时，多种操作会被串接在一起执行。
         *
         * 1.null引用在历史上被引用到程序设计语言中，目的是为了表示变量值的缺失
         * 2.Java8中引用了一个新的类：java.util.Optional<T>，对存在或者缺失的变量进行建模
         * 3.可以使用静态工厂方法Optional.empty、Optional.of以及Optional.ofNullable创建Optional对象
         * 4.Optional类支持多种方法，比如：map、flatMap、filter，它在概念上与Stream类中对应的方法十分的类似。
         * 5.使用Optional会迫使你更积极的应用Optional对象，来应对变量值缺失的问题，最终能更有效的防止空指针异常
         * 6.使用Optional能帮助你设计更好的API，用户只需要阅读方法签名，就能了解该方法是否接受一个Optional类型的值。
         */
    }

    /**
     *
     * @param properties
     * @param name
     * @return
     */
    public static int readDurationByOptional(Properties properties,String name){
        return Optional.ofNullable(properties.getProperty(name))
                .flatMap(OptionalPractice::stringToInt)
                .filter(i -> i > 0)
                .orElse(0);
    }

    /**
     * 需求：readDuration(properties,"a") -> 5
     * 需求：readDuration(properties,"b") -> 0
     * 需求：readDuration(properties,"c") -> 0
     * 需求：readDuration(properties,"d") -> 0
     *
     * 命令式编程方式读取duration的值：实现既复杂又不具备可读性，呈现为多个由if语句以及try/catch块构成的嵌套条件
     * @param properties
     * @param name
     * @return
     */
    public static int readDuration(Properties properties,String name){
        String value = properties.getProperty(name);
        if(value != null){
            try {
                int i = Integer.parseInt(value);
                if(i > 0){
                    return i;
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        return 0;
    }


    /**
     * 建议将多个类似的方法封装到一个工具类中，称之为OptionalUtility,以后不在需要封装笨拙的try/catch逻辑
     * @param s
     * @return
     */
    public static Optional<Integer> stringToInt(String s){
        try {
            return Optional.of(Integer.parseInt(s));
        }catch (NumberFormatException e){
            return Optional.empty();
        }
    }

}
