package com.gallenzhang.stream;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : zhangxq
 * @date : 2018/8/4
 * @description : 综合练习
 */
public class StreamPractice {
    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );


        //(1)找出2011年发生的所有交易，并按交易额排序(从低到高)。
        transactions.stream()
                .filter(d -> d.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList())
                .forEach(System.out::println);

        //(2)交易员都在哪些不同的城市工作过?
        transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList())   //.collect(Collectors.toSet()) 去掉distinct
                .forEach(System.out::println);

        //(3)查找所有来自于剑桥的交易员，并按姓名排序。
        transactions.stream()
                .map(d -> d.getTrader())
                .distinct()
                .filter(d -> d.getCity().equals("Cambridge"))
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList())
                .forEach(System.out::println);

        //(4)返回所有交易员的姓名字符串，按字母顺序排序。
        String tradeStr = transactions.stream()
                .map(d -> d.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("",(a,b) -> a + b);   //.collect(joining())
        System.out.println("tradeStr:" + tradeStr);


        //(5)有没有交易员是在米兰工作的?
        boolean hasMilan = transactions.stream()
                .map(Transaction::getTrader)
                .anyMatch(d -> d.getCity().equals("Milan"));
        System.out.println("hasMilan:" + hasMilan);


        //(6)打印生活在剑桥的交易员的所有交易额。
        transactions.stream()
                .filter(d -> d.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .forEach(System.out::println);


        //(7)所有交易中，最高的交易额是多少?
        int max = transactions.stream()
                .map(Transaction::getValue)
                .reduce(0,Integer::max);
        System.out.println("max value:" + max);


        //(8)找到交易额最小的交易。
        Optional<Transaction> smallestTransaction = transactions.stream()
                .reduce((a,b) -> a.getValue() < b.getValue() ? a:b); //.min(Comparator.comparing(Transaction::getValue))

        System.out.println("min value transaction:" + smallestTransaction.get());
    }

    /**
     * 交易员
     */
    @Getter
    public static class Trader {
        private final String name;
        private final String city;

        public Trader(String n, String c) {
            this.name = n;
            this.city = c;
        }

        public String toString() {
            return "Trader:" + this.name + " in " + this.city;
        }
    }

    /**
     * 交易
     */
    @Getter
    public static class Transaction {
        private final Trader trader;
        private final int year;
        private final int value;

        public Transaction(Trader trader, int year, int value) {
            this.trader = trader;
            this.year = year;
            this.value = value;
        }

        public String toString() {
            return "{" + this.trader + ", " +
                    "year: " + this.year + ", " +
                    "value:" + this.value + "}";
        }
    }
}


