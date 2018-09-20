package com.fypcdr.app.stream.server;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;
import org.json.simple.JSONObject;


import java.util.List;
import java.util.StringJoiner;

public class SparkHash {
    public static List<JSONObject> getCDR(){
        SparkConf conf = new SparkConf().setAppName("TEST_spark").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        Configuration hbaseConf = HBaseConfiguration.create();
        hbaseConf.set(TableInputFormat.INPUT_TABLE, "cdr_test");
        Long x = System.currentTimeMillis();
        JavaPairRDD<ImmutableBytesWritable, Result> javaPairRdd = sc.newAPIHadoopRDD(hbaseConf, TableInputFormat.class,ImmutableBytesWritable.class, Result.class);
        System.out.println(javaPairRdd.count());


        JavaRDD<JSONObject> javaRDD = javaPairRdd.map(new Function<Tuple2<ImmutableBytesWritable,Result>, JSONObject>() {
            public JSONObject call(Tuple2<ImmutableBytesWritable, Result> tuple) throws Exception {
                Result result = tuple._2;
                JSONObject cdr_json = new JSONObject();
                String called_num = Bytes.toString(result.getValue(Bytes.toBytes("called_num"), Bytes.toBytes("called_num")));
                called_num = DigestUtils.sha256Hex(called_num);
                String called_tower = Bytes.toString(result.getValue(Bytes.toBytes("called_tower"), Bytes.toBytes("called_tower")));
                String recipient_num = Bytes.toString(result.getValue(Bytes.toBytes("calling_num"), Bytes.toBytes("calling_num")));
                recipient_num = DigestUtils.sha256Hex(recipient_num);
                String recipient_tower = Bytes.toString(result.getValue(Bytes.toBytes("calling_tower"), Bytes.toBytes("calling_tower")));
                String datetime = Bytes.toString(result.getValue(Bytes.toBytes("date_time"), Bytes.toBytes("date_time")));
                String duration = Bytes.toString(result.getValue(Bytes.toBytes("duration"), Bytes.toBytes("duration")));
                cdr_json.put("called_num", called_num);
                cdr_json.put("called_tower", called_tower);
                cdr_json.put("recipient_num", recipient_num);
                cdr_json.put("recipient_tower", recipient_tower);
                cdr_json.put("datetime", datetime);
                cdr_json.put("duration", duration);
                return cdr_json;
            }
        }); 

        Long y = System.currentTimeMillis();
        System.out.println(y-x);

        return javaRDD.collect();

    }
}
