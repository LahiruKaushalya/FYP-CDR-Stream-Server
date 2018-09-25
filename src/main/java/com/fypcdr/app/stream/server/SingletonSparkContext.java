package com.fypcdr.app.stream.server;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SingletonSparkContext {
    private static JavaSparkContext javaSparkContext;

    private SingletonSparkContext(){
    }

    public static JavaSparkContext getSparkContext(){
        if (javaSparkContext == null){
            SparkConf sparkConf = new SparkConf().setAppName("CDR_STREAM_SERVER").setMaster("local");
            sparkConf.set("spark.testing.memory", "2147480000");
            sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
            javaSparkContext = new JavaSparkContext(sparkConf);
        }
        return javaSparkContext;
    }
}
