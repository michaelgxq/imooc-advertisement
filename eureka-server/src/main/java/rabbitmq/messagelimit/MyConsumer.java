package rabbitmq.messagelimit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @ClassName: 此类即为我们自定义的消费者端，它继承 DefaultConsumer 类
 * @Description: TODO
 * @author: yourName
 * @date: 2020年11月29日 8:48
 */
public class MyConsumer extends DefaultConsumer {
    // 定义数据类型为 Channel 类的成员变量 channel，用于接收从 Consumer 类传递过来的 Channel 类对象
    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);

        this.channel = channel;
    }

    /**
     * 重写 DefaultConsumer 类中的 handleDelivery() 方法
     * @param consumerTag 该形参接收的是当前这个消费者端的标签（其实就可看作是当前这个消费者端的 ID）
     * @param envelope 该形参接收的是生产者端发送消息时携带的一些信息，如 deliveryTag，交换机名，routingKey 等
     * @param properties 该形参接收的就是生产者端发送的消息中的一些配置信息
     * @param body 该形参接收的就是生产者端发送的消息
     * @throws IOException
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);

        System.out.println("生产者端发送的消息是 ========= " + new String(body));

        // 在消费者端中通过 Channel 类对象调用 void basicAck(long deliveryTag, boolean multiple) 方法来手动向 RabbitMQ 发送 Ack（即回执）
        // 在该方法中
        // 形参 deliveryTag 接收当前 RabbitMQ 服务端发送该条消息这个操作的唯一标签（可以等同理解为该条消息的 ID）
        //（我们可以通过 Envelope 类对象来获取该 deliveryTag 的值）
        // 形参 multiple 用于设置是否批量把当前这条消息前所有没有进行 Ack 确认的消息给 RabbitMQ 服务器发送 Ack ( 即回执 )（一般我们都设置为 false）
        channel.basicAck(envelope.getDeliveryTag(), false);
//        channel.basicNack(envelope.getDeliveryTag(), false, false);

    }


}
