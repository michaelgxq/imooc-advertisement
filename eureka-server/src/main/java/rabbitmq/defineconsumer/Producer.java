package rabbitmq.defineconsumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: Producer
 * @Description: TODO
 * @author: yourName
 * @date: 2020年11月22日 16:49
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建 ConnectionFactory ( 即连接工厂 ) 对象
        ConnectionFactory connectionFactory = new ConnectionFactory();

        // 通过 ConnectionFactory ( 即连接工厂 ) 类对象设置连接 RabbitMQ 服务器的相关信息
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 通过 ConnectionFactory ( 即连接工厂 ) 类对象调用 newConnection() 方法来创建 Connection （即连接）类对象
        Connection connection = connectionFactory.newConnection();

        // 通过 Connection 类对象调用 newConnection() 方法来创建 Channel （即信道）类对象
        Channel channel = connection.createChannel();

        // 定义一条消息
        String msg = "Hello RabbitMQ";

        // 通过 Channel （即信道）类对象调用 basicPublish() 方法来发送消息
        // 该 basicPublish() 方法中需要传入交换机名，路由键（即 routingKey）名，配置信息（上面 AMQP 核心概念中的 message 中有介绍），和消息体（消息体必须转为字节数组）
        // 此时
        // 由于我们给交换机名设置为空字符串
        // 所以
        // 我们其实就等于没有设置交换机，而是使用 RabbitMQ 的默认交换机
        channel.basicPublish("", "test001",null, msg.getBytes());

        // 通过 Channel 类对象调用 close() 方法关闭 Channel（即关闭信道）
        channel.close();

        // 通过 Connection 类对象调用 close() 方法关闭 Connection（即关闭连接）
        connection.close();

    }

    // 此时
    // 只有当我们在发送消息时设置的 RoutingKey 的值与交换机和队列之间的 BindingKey 的值相同时，该条消息才能被对应的队列接收
}
