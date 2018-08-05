package com.gallenzhang.collect;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : zhangxq
 * @date : 2018/8/5
 * @description :用流收集数据
 */
public class CollectBasic {

    public static void main(String[] args) {
        /**
         * 按照名义货币进行分组
         */
        //1.指令性风格实现
        Map<Currency,List<Transaction>> transactionsByCurrencies = new HashMap<>();
        for(Transaction transaction:new ArrayList<Transaction>()){
            Currency currency = transaction.getCurrency();
            List<Transaction> transactions = transactionsByCurrencies.get(currency);
            if(transactions == null){
                transactions = new ArrayList<>();
                transactionsByCurrencies.put(currency,transactions);
            }
            transactions.add(transaction);
        }

        //2.Java8函数式实现
        Map<Currency,List<Transaction>> transactionsByCurrencies2 = new ArrayList<Transaction>().stream()
                .collect(Collectors.groupingBy(Transaction::getCurrency));


        /**
         * 函数式编程的优势：关心结果-"做什么"，而不用操心执行的步骤-"如何做"
         *
         * 指令性编程：多级分组的情况下，需要多层嵌套循环和条件，可读性差，代码很难维护及修改
         *
         * 函数式编程只要再加上一个收集器就可以轻松的增强功能了。
         *
         * 函数式API：易复合和重用
         */
    }

    /**
     * 交易
     */
    @Getter
    @Setter
    public class Transaction{
        private Currency currency;
        private int value;
    }

    /**
     * 货币
     */
    @Getter
    @Setter
    public class Currency{
        private String name;
        private String symbol;
    }
}
