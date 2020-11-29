package rabbitmq.returnmessage;

import com.rabbitmq.client.*;

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
        // 该 basicPublish() 方法中需要传入交换机名，路由键（即 routingKey）名，mandatory 属性，配置信息（上面 AMQP 核心概念中的 message 中有介绍），和消息体（消息体必须转为字节数组）
        // 此时
        // 由于我们给交换机名设置为空字符串
        // 所以
        // 我们其实就等于没有设置交换机，而是使用 RabbitMQ 的默认交换机
        // 注意
        // 这里 mandatory 属性一定要设置为 true
        // 否则
        // RabbitMQ 服务会把无法发送的消息给删除
        // 那么
        // 下面重写的 ReturnListener 类中的 handleReturn() 方法就无法接收到该无法发送消息了
        channel.basicPublish("", routingKey,false,null, msg.getBytes());


        // 通过 Channel 类对象调用 addReturnListener() 方法
        // 然后
        // 在该方法中传入一个 ReturnListener 类对象
        // 然后
        // 通过重写 ConfirmListener 类中的 handleReturn() 方法来处理那些 RabbitMQ 服务无法发送的信息
        //（这里使用匿名内部类的方式来实现创建 ConfirmListener 类对象的）
        channel.addReturnListener(new ReturnListener() {
            /**
             * 重写 ReturnListener 类中的 handleReturn() 方法，用于处理那些 RabbitMQ 服务无法发送的信息
             * @param replyCode 该形参接收 RabbitMQ 返回的响应码（不同的无法发送消息的原因，对应不同的响应码）
             * @param replyText 该形参接收 RabbitMQ 返回的响应文本（不同的响应码对应不同的响应文本）
             * @param exchange 该形参接收生产者发送这条无法发送的消息时设置的交换机名
             * @param routingKey 该形参接收生产者发送这条无法发送的消息时设置的 routingKey
             * @param basicProperties 该形参接收生产者发送这条无法发送的消息时设置的消息头
             * @param body 该形参接收生产者发送这条无法发送的消息时的消息体
             * @throws IOException
             */
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] body) throws IOException {
                System.out.println("replyCode " + replyCode);
                System.out.println("replyText " + replyText);
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
