package com.fypcdr.app.stream.server;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;
import org.json.simple.JSONObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.List;

public class SparkHash {
    public static List<JSONObject> getCDR(int start, int end){
        JavaSparkContext sc = SingletonSparkContext.getSparkContext();

        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("conf.properties");
            // load properties file
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Configuration hbaseConf = HBaseConfiguration.create();
        hbaseConf.set(TableInputFormat.INPUT_TABLE, prop.getProperty("table_name"));
        Long x = System.currentTimeMillis();
        JavaPairRDD<ImmutableBytesWritable, Result> javaPairRdd = sc.newAPIHadoopRDD(hbaseConf, TableInputFormat.class,ImmutableBytesWritable.class, Result.class);
        System.out.println(javaPairRdd.count());

        JavaPairRDD<ImmutableBytesWritable, Result> selectedRdd = sc.parallelizePairs(javaPairRdd.collect().subList(start, end));

        JavaRDD<JSONObject> javaRDD = selectedRdd.map(new Function<Tuple2<ImmutableBytesWritable,Result>, JSONObject>() {
            public JSONObject call(Tuple2<ImmutableBytesWritable, Result> tuple) throws Exception {
                Result result = tuple._2;
                JSONObject cdr_json = new JSONObject();
                for(int i=1; i<= Integer.parseInt(prop.getProperty("attribute_count")); i++){
                    String attribute = prop.getProperty("a"+i);
                    String method = prop.getProperty("m"+i);
                    String value = Bytes.toString(result.getValue(Bytes.toBytes(attribute), Bytes.toBytes(attribute)));
                    if(method.equals("SHA256")){
                        value = DigestUtils.sha256Hex(value);
                    }
                    cdr_json.put(prop.getProperty("s"+i), value);
                }
                return cdr_json;
            }
        }); 

        Long y = System.currentTimeMillis();
        System.out.println(y-x);

        return javaRDD.collect();

    }
}
