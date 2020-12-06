package rabbitmq.springboot.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

// 该 RabbitSender 类就是一个生产者
@Component
public class RabbitSender {

	//自动注入 RabbitTemplate 模板类对象
	@Autowired
	private RabbitTemplate rabbitTemplate;  
	
	// 创建一个 ConfirmCallback 类对象
	final ConfirmCallback confirmCallback = new ConfirmCallback() {
		/**
		 * 重写 ConfirmCallback 类中的 confirm() 方法
		 * @param correlationData 该形参接收我们在发送消息时设置的 correlationData（一般它的值是我们发送这条消息中用到的数据库相关表的主键 id）
		 * @param ack 该形参接收 RabbitMQ 发送的回执类型 true 表示发送成功，false 表示发送失败
		 * @param cause 该形参很少用到
		 */
		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause) {
			System.err.println("correlationData: " + correlationData);
			System.err.println("ack: " + ack);
			if(!ack){
				System.err.println("异常处理....");
			}
		}
	};
	
	// 创建一个 ReturnCallback 类对象
	final ReturnCallback returnCallback = new ReturnCallback() {
		/**
		 * 重写 ReturnCallback 类中的 returnedMessage() 方法
		 * @param message 该形参接收我们发送的那条消息（也就是那条发送失败的消息）
		 * @param replyCode 该形参接收 RabbitMQ 发送过来的发送失败码
		 * @param replyText 该形参接收 RabbitMQ 发送过来的发送失败描述
		 * @param exchange 该形参接收接收该条发送失败消息的交换机名
		 * @param routingKey 该形参接收发送这条消息时设置的 routingKey
		 */
		@Override
		public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
			System.err.println("return exchange: " + exchange + ", routingKey: " 
				+ routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
		}
	};
	
	// 定义 send() 方法用于发送消息
	public void send(Object message, Map<String, Object> properties) throws Exception {
		// 创建 MessageHeaders 类对象用于设置消息的属性
		MessageHeaders mhs = new MessageHeaders(properties);

		// 通过 MessageBuilder 类调用 createMessage() 方法创建 Message 类对象
		Message msg = MessageBuilder.createMessage(message, mhs);

		// 通过 RabbitTemplate 类对象调用 setMandatory() 方法设置 mandatory 属性为 true
		rabbitTemplate.setMandatory(true);

		// 通过 RabbitTemplate 类对象调用 setConfirmCallback() 方法设置用于确认消息机制的回调函数（即上面创建的 ConfirmCallback 类对象）
		rabbitTemplate.setConfirmCallback(confirmCallback);

		// 通过 RabbitTemplate 类对象调用 setReturnCallback() 方法设置用于返回消息机制的回调函数（即上面创建的 ReturnCallback 类对象）
		rabbitTemplate.setReturnCallback(returnCallback);

		// 创建 CorrelationData 类对象，构造方法中传入给发送的消息设置的唯一 ID（一般它的值是我们发送这条消息中用到的数据库相关表的主键 id）（这里由于是 demo 所以就随便写了一个）
		CorrelationData correlationData = new CorrelationData("1234567890");

		// 通过 RabbitTemplate 类对象调用 convertAndSend() 方法发送消息
		rabbitTemplate.convertAndSend("exchange-1", "springboot.abc", msg, correlationData);
	}


//	//发送消息方法调用: 构建自定义对象消息
//	public void sendOrder(Order order) throws Exception {
//		rabbitTemplate.setConfirmCallback(confirmCallback);
//		rabbitTemplate.setReturnCallback(returnCallback);
//		//id + 时间戳 全局唯一
//		CorrelationData correlationData = new CorrelationData("0987654321");
//		rabbitTemplate.convertAndSend("exchange-2", "springboot.def", order, correlationData);
//	}

}
