package rabbitmq.messagelimit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

        // 通过 Connection 类对象调用 newConnection() 方法来创建 Channel （即信道）类对象
        Channel channel = connection.createChannel();

        String queueName = "test001";

        // 通过 Channel （即信道）类对象调用 queueDeclare() 方法创建队列
        // 该方法接收一下参数
        // 形参 queue 接收队列名
        // 形参 durable 表示是否持久化（它是一个布尔值）（它表示在我们重启 RabbitMQ 之后，该队列是否还存在）
        // 形参 exclusive 表示是否独占（它是一个布尔值）（它表示该队列是否只有当前 Channel 才能监听，它的作用后面会讲）
        // 形参 autoDelete 表示是否自动删除（它是一个布尔值）（它表示某个队列如果没有和任何交换机有绑定关系，那么就可以把这个队列删除）
        // 形参 arguments 接收该队列的属性
        channel.queueDeclare(queueName,true, false,false, null);


        // 在消费者端调用通过 Channel 类对象调用 void basicQos(int prefetchSize, int prefetchCount, boolean global) 方法来实现限流
        // 在该方法中形参 prefetchSize 用于设置消费者端所消费的消息的大小限制（一般该形参我们都设置为 0（即不做限制））
        // 形参 prefetchCount 用于设置 RabbitMQ 一次给消费者端发送多少条消息（该形参是实现消费端限流的关键），该形参值我们一般都设置为 1
        // ( 即 RabbitMQ 服务器一次只给消费端发送一条消息，只要消费端没有给 RabbitMQ 服务器发送消息处理完成的 Ack（即回执），那么就不会给消费者端发送下一条消息 )
        // 形参 global 用于设置该消费端限流策略的应用级别，设置为 true 表示该策略用于整个信道（即当前该消费者端所在的 Channel ）上
        // 设置为 false 表示该策略仅用于当前消费者端（我们一般也都设置为 false）
        channel.basicQos(0, 1, false);


        // 通过 Channel 类对象调用 basicConsume() 方法，设置队列信息，并处理接收到的信息
        // 该 basicConsume() 方法有三个形参
        // 形参 queue 接收队列名
        // 形参 autoAck 表示是否自动签收（它是一个布尔值）（它表示当消费者接收到消息之后，是否自动给 RabbitMQ 服务器发送回执）（该形参值一般都是设置为 false）
        // 形参 callback 接收一个 Consumer （即消费者）类或者它的子类的对象
        // 方法中传入的第三个参数就是我们自定义的消费者端类对象，构造方法从传入当前这个 channel 类对象
        channel.basicConsume(queueName, false, new MyConsumer(channel));


    }
}
