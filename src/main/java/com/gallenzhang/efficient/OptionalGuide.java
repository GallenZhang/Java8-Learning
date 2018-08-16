package com.gallenzhang.efficient;

import lombok.Getter;

import java.util.Optional;

/**
 * @author : zhangxq
 * @date : 2018/8/13
 * @description :Optional类入门
 */
public class OptionalGuide {
    public static void main(String[] args) {
        /**
         * 代码应该始终如一的使用Optional，能非常清晰的界定出变量值的缺失是结构上的问题，还是你算法上的缺陷，又或者是你数据中的问题。
         * 使用Optional并非要消除每一个null引用，与此相反，它的目标是帮助你更好的设计出普适的API，看到签名就了解它是否接收一个Optional的值。
         */


        //1.创建Optional对象
        Optional<Car> optionalCar = Optional.empty();   //声明一个空的Optional
        optionalCar = Optional.of(new Car());           //根据一个非空值创建Optional
        optionalCar = Optional.ofNullable(new Car());   //可接受null的Optional

        //2.使用map从Optional对象中提取和转换值
        Optional<Insurance> optionalInsurance = Optional.ofNullable(new Insurance());
        Optional<String> name = optionalInsurance.map(Insurance::getName);

        //3.使用flatMap链接Optional对象。这里Person::getCar返回Optional<Car> ，Optional<Optional<Car>>不能调用map方法，
        //使用flatMap方法转成Optional<Car>
        Optional<Person> optionalPerson = Optional.of(new Person());
        name = optionalPerson.flatMap(Person::getCar).flatMap(Car::getInsurance).map(Insurance::getName);
        System.out.println(getCarInsuranceName(Optional.empty()));

        //4.两个Optional对象的组合
        System.out.println(nullSafeFindCheapestInsurance(Optional.empty(),Optional.empty()));

        //5.使用filter剔除特定的值
        /**
         *  以安全的方式检查保险公司的名称是否为"CambridgeInsurance"
         *
         *  Insurance insurance = null;
         *  if(insurance != null && "CambridgeInsurance".equals(insurance.getName())){
         *      System.out.println("ok");
         *  }
         *
         */
        Optional<Insurance> optInsurance = Optional.empty();
        optInsurance.filter(insurance -> "CambridgeInsurance".equals(insurance.getName()))
                .ifPresent(x -> System.out.println("ok"));


        //filter实例
        getCarInsuranceName(Optional.empty(),20);


    }

    /**
     * 找出年龄大于或者等于minAge参数的Person所对应的保险公司列表
     * @param person
     * @param minAge
     * @return
     */
    public static String getCarInsuranceName(Optional<Person> person,int minAge){
        return person.filter(p -> p.getAge() >= minAge)
                .flatMap(p -> p.getCar())
                .flatMap(car -> car.getInsurance())
                .map(insurance -> insurance.getName())
                .orElse("Unknown");
    }

    /**
     * 安全
     * @param person
     * @param car
     * @return
     */
    public static Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person,Optional<Car> car){
        //与之前的null检查太相似，没有充分利用到Optional类提供的特性
        if(person.isPresent() && car.isPresent()){
            return Optional.of(findCheapestInsurance(person.get(),car.get()));
        }else {
            return Optional.empty();
        }

        // 更优雅的解决方案
        // return person.flatMap(p -> car.map(c -> findCheapestInsurance(p,c)));

    }

    public static Insurance findCheapestInsurance(Person person,Car car){
        //不同的保险公司提供的查询服务
        //对比所有数据
        return new Insurance();
    }


    public static String getCarInsuranceName(Optional<Person> person){
        return person.flatMap((d) -> d.getCar())
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
    }


    @Getter
    public static class Person{
        private Optional<Car> car = Optional.empty();

        private int age;
    }

    @Getter
    public static class Car{
        private Optional<Insurance> insurance = Optional.empty();
    }

    @Getter
    public static class Insurance{
        private String name;
    }
}
