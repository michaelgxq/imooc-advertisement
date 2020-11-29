package rabbitmq.returnmessage;

import com.rabbitmq.client.*;

import java.io.IOException;
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


        // 定义一个队列名
        String queueName = "test.rebound";



        // 通过 Channel （即信道）类对象调用 queueDeclare() 方法创建队列
        // 该方法有以下几个形参
        // 形参 queue 接收队列名
        // 形参 durable 表示是否持久化（它是一个布尔值）（它表示在我们重启 RabbitMQ 之后，该队列是否还存在）
        // 形参 exclusive 表示是否独占（它是一个布尔值）（它表示该队列是否只有当前 Channel 才能监听，它的作用后面会讲）
        // 形参 autoDelete 表示是否自动删除（它是一个布尔值）（它表示某个队列如果没有和任何交换机有绑定关系，那么就可以把这个队列删除）
        // 形参 arguments 接收该队列的属性
        channel.queueDeclare(queueName,true, false,false, null);


        // 通过 Channel 类对象调用 basicConsume() 方法，设置队列信息，并处理接收到的信息
        // 该 basicConsume() 方法有三个形参
        // 形参 queue 接收队列名
        // 形参 autoAck 表示是否自动签收（它是一个布尔值）（它表示当消费者接收到消息之后，是否自动给 RabbitMQ 服务器发送回执）（该形参值一般都是设置为 false）
        // 形参 callback 接收一个 Consumer （即消费者）类或者它的子类的对象
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println(new String(body));

                System.out.println(properties.getReplyTo() + "========");


            }
        });


    }
}
