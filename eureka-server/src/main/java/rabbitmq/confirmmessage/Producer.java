package rabbitmq.confirmmessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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

        // 通过 Cnnection 类对象调用 newConnection() 方法来创建 Channel （即信道）类对象
        Channel channel = connection.createChannel();

        String msg = "Hello RabbitMQ";

        // 定义一个 routingKey
        String routingKey = "test.rebound";

        // 通过 Channel 类对象调用 confirmSelect() 方法来开启消息确认模式
        channel.confirmSelect();

        // 通过 Channel （即信道）类对象调用 basicPublish() 方法来发送消息
        // 该 basicPublish() 方法中需要传入交换机名，路由键（即 routingKey）名，配置信息（上面 AMQP 核心概念中的 message 中有介绍），和消息体（消息体必须转为字节数组）
        // 此时
        // 由于我们给交换机名设置为空字符串
        // 所以
        // 我们其实就等于没有设置交换机，而是使用 RabbitMQ 的默认交换机
        channel.basicPublish("", routingKey,null, msg.getBytes());


        // 通过 Channel 类对象调用 addConfirmListener() 方法
        // 然后
        // 在该方法中传入一个 ConfirmListener 类对象
        // 然后
        // 通过重写 ConfirmListener 类中的 handleAck() 方法和 handleNack() 方法来对 RabbitMQ 服务发送的响应进行处理
        //（这里使用匿名内部类的方式来实现创建 ConfirmListener 类对象的）
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 重写 ConfirmListener 类中的 handleAck() 方法，用于处理 RabbitMQ 服务发送信息成功后返回的响应
             * @param deliveryTag 该形参接收当前发送的这条消息的唯一标签（即我们通过该形参来判断消费者端返回的回执是否就是我们之前发送的那条消息）
             * @param multiple 该形参表示是否让 RabbitMQ 服务批量发送回执给生产者
             * @throws IOException
             */
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("========== ack! =============");
            }

            /**
             * 重写 ConfirmListener 类中的 handleNack() 方法，用于处理 RabbitMQ 服务发送信息失败后返回的响应
             * @param deliveryTag 该形参接收当前发送的这条消息的唯一标签（即我们通过该形参来判断消费者端返回的回执是否就是我们之前发送的那条消息）
             * @param multiple 该形参表示是否让 RabbitMQ 服务批量发送回执给生产者
             * @throws IOException
             */
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("========== no ack! =============");
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


        // 此时
        // 一旦 RabbitMQ 服务发送信息成功，那么当前生产者端中的 handleAck() 方法就会接收到 RabbitMQ 服务发送的消息发送成功的响应
        // 一旦 RabbitMQ 服务发送信息失败，那么当前生产者端中的 handleNack() 方法就会接收到 RabbitMQ 服务发送的消息发送失败的响应
    }


}
