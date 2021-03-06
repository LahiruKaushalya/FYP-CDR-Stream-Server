package com.fypcdr.app.stream.server;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.List;

public class SparkHash {
    public static List<JSONObject> getCDR(int start, int end){
        JavaSparkContext sc = SingletonSparkContext.getSparkContext();

        Configuration hbaseConf = HBaseConfiguration.create();
        hbaseConf.set(TableInputFormat.INPUT_TABLE, Server.getProp().getProperty("table_name"));
        
        try {
            //Table must have integer row key
            Scan scan = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
            scan.setCaching(500);
            hbaseConf.set(TableInputFormat.SCAN, TableMapReduceUtil.convertScanToString(scan));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long x = System.currentTimeMillis();
        JavaPairRDD<ImmutableBytesWritable, Result> javaPairRdd = sc.newAPIHadoopRDD(hbaseConf, TableInputFormat.class,ImmutableBytesWritable.class, Result.class);
        
        JavaRDD<JSONObject> javaRDD = javaPairRdd.map(new Function<Tuple2<ImmutableBytesWritable,Result>, JSONObject>() {
            public JSONObject call(Tuple2<ImmutableBytesWritable, Result> tuple) throws Exception {
                Result result = tuple._2;
                JSONObject cdr_json = new JSONObject();
                for(int i=1; i<= Integer.parseInt(Server.getProp().getProperty("attribute_count")); i++){
                    String attribute = Server.getProp().getProperty("a"+i);
                    String method = Server.getProp().getProperty("m"+i);
                    String value = Bytes.toString(result.getValue(Bytes.toBytes(attribute), Bytes.toBytes(attribute)));
                    if(method.equals("SHA256")){
                        value = DigestUtils.sha256Hex(value);
                    }
                    cdr_json.put(Server.getProp().getProperty("s"+i), value);
                }
                return cdr_json;
            }
        }); 

        Long y = System.currentTimeMillis();
        System.out.println(y-x);

        return javaRDD.collect();

    }
}
