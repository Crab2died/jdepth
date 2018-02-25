package com.github.jms.amq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class Producer {

    private static Logger logger = LoggerFactory.getLogger(Producer.class);

    //ActiveMq 的默认用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //ActiveMq 的默认登录密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //ActiveMQ 的链接地址
    private static final String BROKEN_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    //链接对象
    private static Connection connection;
    //事务管理
    private static Session session;

    private static ThreadLocal<MessageProducer> threadLocal = new ThreadLocal<>();

    static {
        try {
            //创建一个链接工厂
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEN_URL);
            //从工厂中创建一个链接
            connection = connectionFactory.createConnection();
            //开启链接
            connection.start();
            /*
             创建一个事务（这里通过参数可以设置事务的级别）
             当第一个参数为true时，表示启用事物，第二个参数默认无效为Session.SESSION_TRANSACTED
             当第一个参数为false时，第二个参数为下列值：
             1. session.AUTO_ACKNOWLEDGE当消费端从receive或者onMessage成功返回时，Session自动签收客户端的这条消息。
             2. Session.CLIENT_ACNOWLEDGE消费端通过调用消息(Message)的acknowledge方法签收消息(mess.acknowledge())。
                在这种情况下，签收发生在Session层面：签收一个已经消费的消息会自动地签收这个Session所有消费已消费消息的收条。
             3. Session.DUPS_OK_ACKNOWLEDGE此选项指示Session不必确保对传送消息的签收，它可能引起消息的重复，
                但是降低了Session的开销，所以只有消费端能容忍重复消息才能使用。
            */
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (JMSException e) {
            logger.error("JMS error: ", e);
        }
    }

    public static void sendQueueMsg(String dest, String msg) {
        try {
            // 创建队列
            Queue queue = session.createQueue(dest);
            // 消息生产者
            MessageProducer messageProducer;
            if (threadLocal.get() != null) {
                messageProducer = threadLocal.get();
            } else {
                messageProducer = session.createProducer(queue);
                //设置生产者的模式，有两种可选
                //DeliveryMode.PERSISTENT 当activemq关闭的时候，队列数据将会被保存
                //DeliveryMode.NON_PERSISTENT 当activemq关闭的时候，队列里面的数据将会被清空
                messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
                threadLocal.set(messageProducer);
            }

            messageProducer.send(session.createTextMessage(msg));
            // 如果无事物执行则不需commit
            session.commit();

        } catch (JMSException e) {
            logger.error("JMS error: ", e);
        }
    }

    public static void publishTopicMsg(String dest, String msg) {

        try {
            Topic topic = session.createTopic(dest);

            MessageProducer producer;
            if (null != threadLocal.get()) {
                producer = threadLocal.get();
            } else {
                producer = session.createProducer(topic);
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                threadLocal.set(producer);
            }
            producer.send(session.createTextMessage(msg));
            session.commit();
        } catch (JMSException e) {
            logger.error("JMS error: ", e);
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (JMSException e) {
            logger.error("JMS error: ", e);
        }
    }
}
