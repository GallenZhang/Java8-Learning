package com.gallenzhang.efficient;

import java.util.concurrent.*;

/**
 * @author : zhangxq
 * @date : 2018/8/16
 * @description :Future使用案例
 */
public class FutureCase {

    public static void main(String[] args) {

        /**
         * 1.Future接口在Java5中被引入，设计初衷是对将来某个时刻会发生的结果进行建模.建模了一种异步计算，返回一个执行运算结果的引用，
         * 当运算结束后，这个引用会被返回给调用方。在Future中触发哪些潜在耗时的操作从线程中释放出来，不用再等到耗时操作的完成。
         * Future比底层的Thread更好用。使用Future，只要把耗时的操作封装到Callable对象中，再提交给ExecutorService就好了。
         */
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Double> future = executor.submit(new Callable<Double>() {
            @Override
            public Double call(){
                //doSomeLongComputation();
                return null;
            }
        });

        try {
            Double result = future.get(1,TimeUnit.SECONDS); //获取异步操作结果，如果最终被阻塞，无法获取结果，则等待1秒后退出
        } catch (InterruptedException e) {
            //当前线程在等待过程中被中断
            e.printStackTrace();
        } catch (ExecutionException e) {
            //计算抛出一个异常
            e.printStackTrace();
        } catch (TimeoutException e) {
            //在Future对象完成之前已经过期
            e.printStackTrace();
        }

        /**
         * 2.Future接口的局限性
         * 很难表述Future结果之间的依赖性，如："当长时间计算任务完成时，请将该计算的结果通知到另一个长时间运行的计算任务，
         * 这两个计算任务都完成后，将计算的结果与另一个查询操作结果合并"。但是使用Future提供的方法完成这样的操作又是另外一回事。
         *
         * 我们需要更具有描述能力的特性，比如下面这些：
         * 1）将两个异步计算合并为一个--这两个异步计算之间相互独立，同时第二个又依赖于第一个的结果。
         * 2）等待Future集合中的所有任务都完成。
         * 3）仅等待Future集合中最快结束的任务完成，并返回它的结果。
         * 4）通过编程方式完成一个Future任务的执行。
         * 5）应对Future的完成事件（当Future完成事件发生时会收到通知，并能使用Future计算的结果进行下一步操作，不只是简单的阻塞等待操作结果）
         *
         * Java8中CompletableFuture（它实现了Future接口）的新特性以更直观的方式将上述需求都变为可能。
         */
    }
}
