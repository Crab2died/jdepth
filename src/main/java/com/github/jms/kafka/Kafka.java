//package com.github.jms.kafka;
//
//import kafka.javaapi.producer.Producer;
//import kafka.producer.KeyedMessage;
//import kafka.producer.ProducerConfig;
//
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class Kafka {
//
//    public static void main(String... args) {
//        String brokerList = "xjtz234:9091,xjtz234:9092,xjtz234:9093";
//        Properties props = new Properties();
//        props.put("metadata.broker.list", brokerList);
//        /*
//         * 0表示不等待结果返回<br/>
//         * 1表示等待至少有一个服务器返回数据接收标识<br/>
//         * -1表示必须接收到所有的服务器返回标识，及同步写入<br/>
//         */
//        props.put("request.required.acks", "1");
//        /*
//         * 内部发送数据是异步还是同步
//         * sync：同步, 默认
//         * async：异步
//         */
//        props.put("producer.type", "async");
//        /*
//         * 设置序列化的类
//         * 可选：kafka.serializer.StringEncoder
//         * 默认：kafka.serializer.DefaultEncoder
//         */
//        props.put("serializer.class", "kafka.serializer.StringEncoder");
//        /*
//         * 设置分区类
//         * 根据key进行数据分区
//         * 默认是：kafka.producer.DefaultPartitioner ==> 安装key的hash进行分区
//         * 可选:kafka.serializer.ByteArrayPartitioner ==> 转换为字节数组后进行hash分区
//         */
//        // props.put("partitioner.class", "com.github.jms.kafka.KafkaProducerPartitioner");
//
//        // 重试次数
//        props.put("message.send.max.retries", "3");
//
//        // 异步提交的时候(async)，并发提交的记录数
//        props.put("batch.num.messages", "200");
//
//        // 设置缓冲区大小，默认10KB
//        props.put("send.buffer.bytes", "102400");
//
//        // 2. 构建Kafka Producer Configuration上下文
//        ProducerConfig config = new ProducerConfig(props);
//
//        // 3. 构建Producer对象
//        final Producer<String, String> producer = new Producer<>(config);
//
//        int numThreads = 10;
//        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
//
//        CountDownLatch latch = new CountDownLatch(5);
//        for (int i = 0; i < 5; i++) {
//            int finalI = i;
//            pool.submit(new Thread(() -> {
//
//                // 发送数据
//                KeyedMessage message = new KeyedMessage("topic1", "key1" + finalI, "v1");
//                producer.send(message);
//                System.out.println("发送数据:" + message);
//
//                latch.countDown();
//            }, "Thread-" + i));
//        }
//
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        producer.close();
//    }
//}
