package rabbitmq.mqmessage;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
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

        // 通过 Cnnection 类对象调用 newConnection() 方法来创建 Channel （即信道）类对象
        Channel channel = connection.createChannel();

        String msg = "Hello RabbitMQ";

        // 定义一个 routingKey
        String routingKey = "test.rebound";


        // 由于生产者需要从消费者那里接受回执
        // 所以
        // 我们也就需要在生产者中定义一个队列和交换机进行绑定，用于接收消费者发送过来的回执
        // 所以
        // 此时生产者同时也是消费者

        // 定义一个队列名
        String queueName = "producerQueue";

        // 通过 Channel （即信道）类对象调用 queueDeclare() 方法创建队列
        // 该方法有以下几个形参
        // 形参 queue 接收队列名
        // 形参 durable 表示是否持久化（它是一个布尔值）（它表示在我们重启 RabbitMQ 之后，该队列是否还存在）
        // 形参 exclusive 表示是否独占（它是一个布尔值）（它表示该队列是否只有当前 Channel 才能监听，它的作用后面会讲）
        // 形参 autoDelete 表示是否自动删除（它是一个布尔值）（它表示某个队列如果没有和任何交换机有绑定关系，那么就可以把这个队列删除）
        // 形参arguments 接收扩展参数（该形参很少用到）
        channel.queueDeclare(queueName, true, false, false, null);


        // 通过 AMQP.BasicProperties() 为消息设置 message-id 和 reply-to 这两个属性
        // 其中
        // message-id 属性的值就是一个 UUID 字符串（它就相当于当前这条消息的 ID）
        // reply-to 属性的值就是我们当前在这个生产者中创建的队列的名字（就是上面定义的队列名）
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().messageId(UUID.randomUUID().toString()).replyTo(queueName).build();


        // 通过 Channel （即信道）类对象调用 basicPublish() 方法来发送消息
        // 该 basicPublish() 方法中需要传入交换机名，路由键（即 routingKey）名，配置信息（上面 AMQP 核心概念中的 message 中有介绍），和消息体（消息体必须转为字节数组）
        // 此时
        // 由于我们给交换机名设置为空字符串
        // 所以
        // 我们其实就等于没有设置交换机，而是使用 RabbitMQ 的默认交换机
        channel.basicPublish("", routingKey,properties, msg.getBytes());


        // 通过 Channel 类对象调用 basicConsume() 方法，设置队列信息，并处理从消费者发送过来的信息
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {

                System.out.println("收到消费者发送过来的消息 === " + new String(body));

            }
        });


        // 由于
        // 生产者需要通过监听消费者发送过来的消息
        // 所以
        // 我们这里不能把信道和连接对象关闭
        // 所以
        // 我们要把下面两行给注释掉


        // 通过 Channel 类对象调用 close() 方法关闭 Channel（即关闭信道）
//        channel.close();
        // 通过 Connection 类对象调用 close() 方法关闭 Connection（即关闭连接）
//        connection.close();


    }


}
