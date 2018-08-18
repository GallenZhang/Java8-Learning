package com.gallenzhang.efficient;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : zhangxq
 * @date : 2018/8/16
 * @description :CompletableFuture使用示例
 */
public class CompletableFuturePractice {

    /**
     * 商店
     */
    public static List<Shop> shopList = Arrays.asList(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"),
            new Shop("ShopEasy"),
            new Shop("ShopAll"),
            new Shop("GoToShop"),
            new Shop("I love shop"),
            new Shop("Lets shop"),
            new Shop("Lets buy")
    );

    /**
     * 使用CompletableFuture来实现一个"最佳价格查询器"的应用（查询多个在线商店，依据给定的产品或者服务找出最低的价格）
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 同步API：调用了某个方法，调用方在被调用方运行的过程中会一直等待，被调用方运行结束返回，调用方取得被调用方的返回值并继续运行。（阻塞式调用）
         *
         * 异步API：调用后直接返回，剩余的计算任务交给另一个线程去做，另一个线程将计算结果返回给调用方，返回方式要么是通过回调方式，要么是由调用方再执行
         * 一个"等待，直到计算完成"的方法调用。（非阻塞式调用）
         */

        //1.使用异步API
        Shop shop = new Shop("BestShop");
        long start = System.currentTimeMillis();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long cost = System.currentTimeMillis() - start;
        System.out.println("Invocation returned after " + cost + " ms");

        //执行更多任务，比如查询其他商店
        //doSomethingElse();

        //在计算商品价格的同时
        try {
            double price = futurePrice.get();
            System.out.println(String.format("Price is %.2f%n", price));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        long retrievalTime = System.currentTimeMillis() - start;
        System.out.println("Price returned after " + retrievalTime + " ms");


        /**
         * 2.让代码免收阻塞之苦：将同步又缓慢的服务转换成异步的服务
         * Shop类只能提供同步阻塞式的API方法，如何使用异步的方式来查询多个商店，避免被单一的请求阻塞，提供
         * "最佳价格查询器"的性能和吞吐量
         *
         */
        start = System.currentTimeMillis();
        System.out.println(findPrices("myPhone27S"));
        System.out.println("Done in " + (System.currentTimeMillis() - start) + " ms");

        /**
         * 并行 --- 使用流还是CompletableFuture ？
         *
         * 1）.如果进行的是计算密集型的操作，并且没有IO，那么推荐使用Stream接口，因为实现简单，同时效率可能也是最高的
         * （如果所有的线程都是计算密集型的，那么就没有必要创建比处理器核数更多的线程）。
         *
         * 2）.如果并行的工作单元还涉及等到I/O操作（包括网络连接等待），那么使用CompletableFuture 灵活性更好，根据等待/计算，或者
         * W/C的比率来设置需要使用的线程数。
         *
         */


        /**
         * 3.对多个异步任务进行流水线操作
         */
        start = System.currentTimeMillis();
        System.out.println(Discount.findPrices("myPhone27S"));
        System.out.println("Done in " + (System.currentTimeMillis() - start) + " ms");


        /**
         * 4.将两个CompletableFuture对象整合起来，无论它们是否存在依赖。
         *
         *  1）对于第二个CompletableFuture需要依赖第一个CompletableFuture结果值时，使用thenCompose
         *  2）对于两个完全不相干的CompletableFuture对象的结果整合起来，不希望等到第一个任务完全结束才开始第二项任务，可以使用thenCombine，它接收名为
         *  BiFunction的第二个参数，这个参数定义了两个CompletableFuture对象完成计算后，结果如何合并。同thenCompose一样，thenCombine方法也提供了
         *  一个Async的版本。这里如果使用thenCombineAsync会导致BiFunction中定义的合并操作被提交到线程池中，由另一个任务以异步的方式执行。
         *
         *
         *  例子：商店查询指定商品的价格，同时从远程的汇率服务查到欧元与美元之间的汇率，当二者都结束时，再将这两个结果结合起来，用返回的商品价格乘以当时的汇率，
         *  得到以美元计价的商品价格。
         *
         */
        Future<Double> futurePriceInUSD = CompletableFuture.supplyAsync(() -> Shop.getPrice("iPhone8"))
                .thenCombine(
                  CompletableFuture.supplyAsync(() -> getRate()) ,
                        (price, rate) -> price * rate
                );

        /**
         * 使用Java7实现
         */
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Double> future = executor.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return getRate();
            }
        });
        futurePriceInUSD = executor.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                double priceInEUR = Shop.getPrice("iPhone8");
                return priceInEUR * future.get();
            }
        });
        executor.shutdown();


        /**
         * 5.价格查询器的优化：只要有商店返回商品价格就在第一时间返回显示值，不用等待那些还未返回的商店。
         *
         */
        long finalStart = System.currentTimeMillis();
        CompletableFuture[] futures = Discount.findPricesStream("myPhone27s")
                .map(f -> f.thenAccept(s -> System.out.println(s + " (done in " + (System.currentTimeMillis() - finalStart) + " ms)")))
                .toArray(size -> new CompletableFuture[size]);
        CompletableFuture.allOf(futures).join();
        System.out.println("All shops have now responded in " + (System.currentTimeMillis() - finalStart) + " ms");

    }

    /**
     * 汇率转换
     * @return
     */
    public static double getRate(){
        return new Random().nextDouble() * 10;
    }

    /**
     * 某件商品在所有商店的价格
     * @param product
     * @return
     */
    public static List<String> findPrices(String product){
        /**
         *
         * Done in 10048 ms
         *
            return shopList.parallelStream()
                .map(shop -> String.format("%s price is %.2f",shop.getName(),shop.getPrice(product)))
                .collect(Collectors.toList());
         */


        /**
         * 使用并行流对请求进行并行操作
         * (我的电脑8核处理器) Runtime.getRuntime().availableProcessors()
         *
         * Done in 2016 ms
         *
            return shopList.parallelStream()
                .map(shop -> String.format("%s price is %.2f",shop.getName(),shop.getPrice(product)))
                .collect(Collectors.toList());
         */

        /**
         * 使用CompletableFuture实现findPrices方法
         *
         * Done in 2017 ms

            List<CompletableFuture<String>> priceFutures = shopList.stream()
                    .map(shop -> CompletableFuture.supplyAsync(
                            () -> shop.getName() + " price is " + shop.getPrice(product)))
                    .collect(Collectors.toList());

            return priceFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
         */


        /**
         *
         * 测试证明CompletableFuture版本程序和并行流版本程序相比，差不多。因为他们内部采用同样的通用线程池，默认都使用固定的线程数，
         * 具体取决于Runtime. getRuntime().availableProcessors()的返回值
         *
         * 然而CompletableFuture具有一定的优势，因为允许你对执行器（Executor）进行配置，尤其是线程池的大小，让它更适合应用需求的
         * 方式进行配置，满足程序的需求，而这是并行流API无法提供的
         *
         *
         */

        /**
         * 使用定制的执行器:Done in 1017 ms
         *
         * 如何选择线程数？
         * 《Java并发编程实战》(一书中，Brian Goetz和合著者们为线程池大小 的优化提供了不少中肯的建议。
         * 这非常重要，如果线程池中线程的数量过多，最终它们会竞争 稀缺的处理器和内存资源，浪费大量的时间在上下文切换上。
         * 反之，如果线程的数目过少，正如你的应用所面临的情况，处理器的一些核可能就无法充分利用。
         * Brian Goetz建议，线程池大 小与处理器的利用率之比可以使用下面的公式进行估算:
         * Nthreads = NCPU * UCPU * (1 + W/C)
         * 其中:
         * 1）NCPU是处理器的核的数目，可以通过Runtime.getRuntime().availableProcessors()得到
         * 2）UCPU是期望的CPU利用率(该值应该介于0和1之间)
         * 3）W/C是等待时间与计算时间的比率
         *
         *
         * 应用在99%的时间都在等待商店的响应，所以估算出的W/C为99，如果期望的CPU利用率为100%，则需要创建800个线程的线程池。
         * 实际操作中，如果创建的线程数比商店的数目更多，反而是一种浪费。
         * 这里建议将执行器使用的线程数，与需要查询的商店数目设定为同一个值，这样每个商店都对应一个线程服务。
         * 不过为了避免由于商店的数目过多导致服务器超负荷而崩溃，还需要设置一个上限，比如100个线程
         */
        Executor executor = Executors.newFixedThreadPool(Math.min(shopList.size(), 100),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setDaemon(true);  //设置守护线程，这种方式不会阻止程序的关停
                        return t;
                    }
                });
        List<CompletableFuture<String>> priceFutures = shopList.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getName() + " price is " + shop.getPrice(product),executor))
                .collect(Collectors.toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }



    /**
     * 模拟延迟1s的方法
     */
    public static void delay(){
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 0.5-2.5随机延迟
     */
    public static void randomDelay(){
        int delay = 500 + new Random().nextInt(2000);
        try{
            Thread.sleep(delay);
        }catch (InterruptedException e){
            throw  new RuntimeException(e);
        }
    }

    @Getter
    @Setter
    public static class Quote{
        private final String shopName;
        private final double price;
        private final Discount.Code discountCode;

        public Quote(String shopName,double price,Discount.Code code) {
            this.shopName = shopName;
            this.price = price;
            this.discountCode = code;
        }

        /**
         * 商店返回的字符串解析操作封装到Quote类中
         * @param s
         * @return
         */
        public static Quote parse(String s){
            String[] split = s.split(":");
            String shopName = split[0];
            double price = Double.parseDouble(split[1]);
            Discount.Code discountCode = Discount.Code.valueOf(split[2]);
            return new Quote(shopName,price,discountCode);
        }
    }

    @Getter
    @Setter
    public static class Shop{
        private String name;

        public Shop(String name){
            this.name = name;
        }

        /**
         * getPrice现在以ShopName:price:DiscountCode的格式返回一个String类型的值。
         * @param product
         * @return
         */
        public String getPriceStr(String product){
            double price = calculatePrice(product);
            Discount.Code code = Discount.Code.values()[new Random().nextInt(Discount.Code.values().length)];
            return String.format("%s:%.2f:%s", name, price, code);
        }

        /**
         * 获取价格（同步接口）
         * @param product
         * @return
         */
        public static double getPrice(String product){
            return calculatePrice(product);
        }

        /**
         * 获取价格（异步接口）
         * @param product
         * @return
         */
        public static Future<Double> getPriceAsync(String product){
            CompletableFuture<Double> futurePrice = new CompletableFuture<>();
            new Thread(() -> {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            }).start();


            //计算过程中错误处理，采用completeExceptionally进行处理
            new Thread(() -> {
                try {
                    double price = calculatePrice(product);
                    futurePrice.complete(price);
                }catch (Exception e){
                    futurePrice.completeExceptionally(e); //客户端会收到一个ExecutionException
                }
            }).start();

            //return futurePrice;


            /**
             * CompletableFuture提供了工厂方法创建CompletableFuture对象，提供了同样的错误管理机制 （----推荐使用----）
             */
            return CompletableFuture.supplyAsync(() -> calculatePrice(product));
        }

        /**
         * 根据商品名称计算价格（这里引用了模拟的延迟）
         * @param product
         * @return
         */
        private static double calculatePrice(String product){
            randomDelay();
            //if (true) throw new RuntimeException("product not available");
            return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
        }
    }

    /**
     * 定义折扣类
     */
    public static class Discount{
        public enum Code{
            NONE(0),SILVER(5),GOLD(10),PLATINUM(15),DIAMOND(20);
            private final int percentage;
            Code(int percentage){
                this.percentage = percentage;
            }
        }

        /**
         * 接收Quote对象，返回一个字符串，表示生成该Quote的shop中的折扣价格
         * @param quote
         * @return
         */
        public static String applyDiscount(Quote quote){
            return quote.getShopName() + " price is " + Discount.apply(quote.getPrice(),quote.getDiscountCode());
        }

        /**
         * 计算折扣后的价格
         * @param price
         * @param code
         * @return
         */
        private static double apply(double price, Discount.Code code){
            delay();
            return price * (100 - code.percentage) / 100;
        }

        /**
         * 返回一个由Future构成的流
         * @param product
         * @return
         */
        public static Stream<CompletableFuture<String>> findPricesStream(String product){
            Executor executor = Executors.newFixedThreadPool(Math.min(shopList.size(), 100),
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setDaemon(true);  //设置守护线程，这种方式不会阻止程序的关停
                            return t;
                        }
                    });
            return shopList.stream()
                    .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceStr(product),executor))
                    .map(future -> future.thenApply(Quote::parse))
                    .map(future -> future.thenCompose(
                            quote -> CompletableFuture.supplyAsync(() -> applyDiscount(quote),executor)
                    ));
        }

        /**
         *
         * @param product
         * @return
         */
        public static List<String> findPrices(String product){
            /**
             * Done in 20075 ms
             *
                return shopList.stream()
                    .map(shop -> shop.getPriceStr(product))
                    .map(str -> Quote.parse(str))
                    .map(Discount::applyDiscount)
                    .collect(Collectors.toList());
             */


            /**
             * Done in 2016 ms
             *
             * CompletableFuture API 提供了名为thenCompose的方法，它允许你对两个异步操作进行流水线，第一个操作完成时，将其结果作为参数传递给第二个操作。
             *
             * 你可以创建两个CompletableFuture对象，对第一个CompletableFuture对象调用thenCompose，并向其传递一个函数。当第一个CompletableFuture执行
             * 完毕后，它的结果将作为该函数的参数，这个函数的返回值是以第一个CompletableFuture返回做输入计算的第二个CompletableFuture对象。
             */

            Executor executor = Executors.newFixedThreadPool(Math.min(shopList.size(), 100),
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setDaemon(true);  //设置守护线程，这种方式不会阻止程序的关停
                            return t;
                        }
                    });
            List<CompletableFuture<String>> priceFuture = shopList.stream()
                    .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceStr(product),executor))
                    .map(future -> future.thenApply(Quote::parse))
                    .map(future -> future.thenCompose(quote ->
                            CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote),executor)))
                    .collect(Collectors.toList());

            return priceFuture.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());


            /**
             * thenCompose方法像CompletableFuture类中其他方法一样，也提供了一个以Async后缀结尾版本thenComposeAsync。通常而言，名称中不带
             * Async的方法和它的前一个任务一样，在同一个线程中运行；而名称以Async结尾的方法会将后续的任务提交到一个线程池，所以每个任务是由不同的线程处理。
             *
             * 对于这个例子来说，第二个CompletableFuture对象的结果取决于第一个CompletableFuture，所以无论使用哪个版本方法来处理CompletableFuture对象，
             * 对于最终结果都没有太大的区别。这里选择thenCompose方法的原因是它高效一些，减少了很多线程切换的开销。
             */
        }
    }
}
