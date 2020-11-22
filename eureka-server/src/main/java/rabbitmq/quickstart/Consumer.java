package rabbitmq.quickstart;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: Cosumer
 * @Description: 此为消费者类
 * @author: yourName
 * @date: 2020年11月22日 16:50
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建 ConnectionFactory ( 即连接工厂 ) 对象
        ConnectionFactory connectionFactory = new ConnectionFactory();

        // 通过 ConnectionFactory ( 即连接工厂 ) 类对象设置连接 RabbitMQ 服务器的相关信息
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 通过 ConnectionFactory ( 即连接工厂 ) 类对象调用 newConnection() 方法来创建 Connection （即连接）类对象
        Connection connection = connectionFactory.newConnection();

        // 通过 Cnnection 类对象调用 newConnection() 方法来创建 Channel （即信道）类对象
        Channel channel = connection.createChannel();

        String queueName = "test001";

        // 通过 Channel （即信道）类对象调用 queueDeclare() 方法创建队列
        channel.queueDeclare(queueName,true, false,false, null);


        // 通过 Channel 类对象调用 basicConsume() 方法，设置队列信息，并处理接收到的信息
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {

                System.out.println(Arrays.toString(body));
            }
        });


    }
}
