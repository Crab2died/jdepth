package com.github.crab2died;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class InfluxDBTest {

    @Test
    public void test() throws InterruptedException {
        String databaseURL = "http://10.100.61.249:8086";
        InfluxDB influxDB = InfluxDBFactory.connect(databaseURL);
        Pong response = influxDB.ping();
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            System.out.println("Error pinging server.");
            return;
        }
        System.out.println(response.getVersion());

       // influxDB.createDatabase("LOG_DB");

        Map<String, String> tags = new HashMap<>();
        Map<String, Object> fields = new HashMap<>();

        String[] ACCOUNT_ARR = {"A-0000000001", "A-0000000002", "A-0000000003", "A-0000000004"};

        String[] USER_ARR = {"U-00000000010", "U-00000000011", "U-00000000012", "U-00000000020", "U-00000000021", "U-00000000030", "U-00000000040"};

        String[] NETWORK_TYPE = {"N/A", "Wired", "WIFI", "3G", "4G"};
        String[] VOICE_CODEC = {"G711", "OPUS", "G722", "G729"};


//        BatchPoints batchPoints = BatchPoints
//                .database("LOG_DB")
//                .tag("async", "true")
//                .retentionPolicy("default")
//                .consistency(InfluxDB.ConsistencyLevel.ALL)
//                .build();

        for (int i = 0; i < 100000; i++){
            int user_idx = new Random().nextInt(1000) % 7;
            tags.put("TAG_USER", USER_ARR[user_idx]);
            tags.put("TAG_ACCOUNT", ACCOUNT_ARR[user_idx % 4]);
            tags.put("TAG_NT", NETWORK_TYPE[new Random().nextInt(5)]);
            tags.put("TAG_VC", VOICE_CODEC[new Random().nextInt(4)]);

            fields.put("networkDelay", new Random().nextInt(1000));
            fields.put("networkType", NETWORK_TYPE[new Random().nextInt(5)]);
            fields.put("MOS", new Random().nextInt(20));

            Point point = Point.measurement("CALL_LOG")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag(tags)
                    .fields(fields)
                    .build();
            System.out.println(point);
            influxDB.write("mytest", "autogen", point);
            TimeUnit.MILLISECONDS.sleep(1);
        }
    }
}
