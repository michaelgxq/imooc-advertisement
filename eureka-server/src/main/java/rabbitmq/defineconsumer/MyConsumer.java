package rabbitmq.defineconsumer;

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
    public MyConsumer(Channel channel) {
        super(channel);
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

    }


}
