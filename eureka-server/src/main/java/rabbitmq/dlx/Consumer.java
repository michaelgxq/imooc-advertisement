package rabbitmq.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Consumer {

	
	public static void main(String[] args) throws Exception {

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
		
		// 定义一个普通的交换机的名字 和 队列名 以及 RoutingKey
		String exchangeName = "test_dlx_exchange";
		String routingKey = "dlx.#";
		String queueName = "test_dlx_queue";

		// 通过对象 channel 调用 exchangeDeclare() 方法用于创建一个交换机
		// ========== 注意 ===========
		// 该交换机就是一个普通的交换机，不是死信队列
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);

		// 创建一个 Map 集合，用于设置队列的属性
		Map<String, Object> agruments = new HashMap<>();
		// 往该 Map 集合中存放 x-dead-letter-exchange 属性和它对应的值 dlx.exchange（这个就是死信队列名）
		agruments.put("x-dead-letter-exchange", "dlx.exchange");


		// 通过 Channel （即信道）类对象调用 queueDeclare() 方法创建队列
		// 该方法接收一下参数
		// 形参 queue 接收队列名
		// 形参 durable 表示是否持久化（它是一个布尔值）（它表示在我们重启 RabbitMQ 之后，该队列是否还存在）
		// 形参 exclusive 表示是否独占（它是一个布尔值）（它表示该队列是否只有当前 Channel 才能监听，它的作用后面会讲）
		// 形参 autoDelete 表示是否自动删除（它是一个布尔值）（它表示某个队列如果没有和任何交换机有绑定关系，那么就可以把这个队列删除）
		// 形参 arguments 接收该队列的属性
		// ========== 注意 ===========
		// 该队列就是一个普通的队列，该队列中产生的死信会发送到死信队列中
		channel.queueDeclare(queueName, true, false, false, agruments);

		// 通过 Channel （即信道）类对象调用 queueBind() 方法来把队列和交换机进行绑定
		channel.queueBind(queueName, exchangeName, routingKey);


		// ========== 注意 ===========
		// 以下三行就是创建死信队列 和 接收死信队列发送消息的队列，以及把两者进行绑定的代码
		channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
		channel.queueDeclare("dlx.queue", true, false, false, null);
		channel.queueBind("dlx.queue", "dlx.exchange", "#");


		// 通过 Channel 类对象调用 basicConsume() 方法，设置队列信息，并处理接收到的信息
		// 该 basicConsume() 方法有三个形参
		// 形参 queue 接收队列名
		// 形参 autoAck 表示是否自动签收（它是一个布尔值）（它表示当消费者接收到消息之后，是否自动给 RabbitMQ 服务器发送回执）（该形参值一般都是设置为 false）
		// 形参 callback 接收一个 Consumer （即消费者）类或者它的子类的对象
		// 方法中传入的第三个参数就是我们自定义的消费者端类对象，构造方法从传入当前这个 channel 类对象
		channel.basicConsume(queueName, true, new MyConsumer(channel));
	}
}
