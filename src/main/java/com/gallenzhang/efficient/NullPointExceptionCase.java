package com.gallenzhang.efficient;

import lombok.Getter;

/**
 * @author : zhangxq
 * @date : 2018/8/13
 * @description :空指针案例
 */
public class NullPointExceptionCase {

    public static void main(String[] args) {
        System.out.println(getCarInsuranceName(null));  //Exception in thread "main" java.lang.NullPointerException

        /**
         * 对每个变量都进行深层次的检查。这种方式不具备扩展性，同时牺牲了代码的可读性。
         */
        System.out.println(getCarInsuraneNameSafe(null));

        /**
         * 虽然避免了深层递归的if语句块，但是判断路程极易出错，如果忘记了检查那个可能为null的属性也会出错。
         */
        System.out.println(getCarInsuraneNameSafeByReturn(null));

        /**
         * 使用null表示变量的缺失是大错特错的，所以需要更优雅的方式来对缺失的变量值建模。
         *
         * Java开发中使用null会带来 理论上和实际上的种种问题：
         * 1.NullPointException是目前Java程序开发中最典型的异常。
         * 2.让你的代码膨胀，到处都充斥着null检查，可读性糟糕透顶
         * 3.null本身没有任何语义。在静态语言中，null以一种错误的方式对缺失变量值的建模
         * 4.破坏了Java的哲学。Java一直试图避免让程序员意识到的指针的存在，空指针是惟一的例外
         * 5.它在Java的类型系统上开了个口子：null并不属于任何类型，这意味着它可以被赋值给任意引用类型的变量。但这个变量被传递到系统的另一部分后，你将
         * 无法获知这个null变量最初始的赋值到底是什么类型。
         */
    }

    @Getter
    public class Person{
        private Car car;
    }

    @Getter
    public class Car{
        private Insurance insurance;
    }

    @Getter
    public class Insurance{
        private String name;
    }

    /**
     * 可能发生NullPointException
     * @param person
     * @return
     */
    public static String getCarInsuranceName(Person person){
        return person.getCar().getInsurance().getName();
    }

    /**
     * null的安全检查
     * @param person
     * @return
     */
    public static String getCarInsuraneNameSafe(Person person){
        if(person != null){
            Car car = person.getCar();
            if(car != null){
                Insurance insurance = car.getInsurance();
                if(insurance != null){
                    return insurance.getName();
                }
            }
        }
        return "Unknown";
    }

    /**
     * null安全检查的第二种尝试
     * @param person
     * @return
     */
    public static String getCarInsuraneNameSafeByReturn(Person person){
        if(person == null){
            return "Unknown";
        }

        Car car = person.getCar();
        if(car == null){
            return "Unknown";
        }

        Insurance insurance = car.getInsurance();
        if(insurance == null){
            return "Unknown";
        }

        return insurance.getName();
    }

}
