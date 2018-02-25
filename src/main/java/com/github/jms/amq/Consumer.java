package com.github.jms.amq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class Consumer {

    private static Logger logger = LoggerFactory.getLogger(Consumer.class);

    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    private static final String BROKEN_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    private static Session session;

    private static ThreadLocal<MessageConsumer> threadLocal = new ThreadLocal<>();

    static {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEN_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            logger.error("JMS error: ", e);
        }
    }

    public static void getQueueMsg(String dest) {
        try {
            Queue queue = session.createQueue(dest);
            MessageConsumer consumer;

            if (threadLocal.get() != null) {
                consumer = threadLocal.get();
            } else {
                consumer = session.createConsumer(queue);
                threadLocal.set(consumer);
            }
            for (; ; ) {
                TextMessage msg = (TextMessage) consumer.receive();
                if (null != msg) {
                    // 签收消息
                    // msg.acknowledge();
                    System.out.println(Thread.currentThread().getName() + ",消息已接收：" + msg.getText());
                }
            }
        } catch (JMSException e) {
            logger.error("JMS error: ", e);
        }
    }

    public static void subscribeTopicMsg(String dest) {
        try {
            Topic topic = session.createTopic(dest);
            MessageConsumer consumer;
            if (threadLocal.get() != null) {
                consumer = threadLocal.get();
            } else {
                consumer = session.createConsumer(topic);
                threadLocal.set(consumer);
            }
            for (; ; ) {
                TextMessage msg = (TextMessage) consumer.receive();
                if (null != msg) {
                    // 接收消息
                    System.out.println(Thread.currentThread().getName() + ",接收订阅消息：" + msg.getText());
                }
            }
        } catch (JMSException e) {
            logger.error("JMS error: ", e);
        }
    }
}
