package com.gallenzhang.collect;

/**
 * @author : zhangxq
 * @date : 2018/8/6
 * @description : 比较收集器的性能
 */
public class CollectorHarness {
    public static void main(String[] args) {
        long fastest = Long.MAX_VALUE;

        for(int i=0;i < 10;i++){
            long start = System.nanoTime();
            //Partitioning.partitionPrimes(1000000);                            //448ms
            CustomCollector.partitionPrimesWithCustomCollector(1000000);     //250ms
            long duration = (System.nanoTime() - start) / 1000000;
            if(duration < fastest){
                fastest = duration;
            }
        }
        System.out.println("Fastest execution done in " + fastest + " msecs");
    }
}
