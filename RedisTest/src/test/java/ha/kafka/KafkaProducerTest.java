package ha.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.Test;

import ha.BaseTest;
/**
 * Kafka生产者
 * @author wangym
 *
 */
public class KafkaProducerTest extends BaseTest {

	Properties props = new Properties();

	@Before
	public void initKafka() {
		//props.put("bootstrap.servers", "192.168.229.13:9092");
		props.put("bootstrap.servers", "192.168.229.13:9092,192.168.229.13:9093,192.168.229.13:9094");
		props.put("acks", "all");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	}

	// 发送信息
	@Test
	public void test1() {
		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			producer.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i), Integer.toString(i)));
		}
		producer.close();
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("共计耗时：" + time + "ms");
	}

	@Test
	public void test2() {
		props.put("transactional.id", "my-transactional-id");
		Producer<String, String> producer = new KafkaProducer<String, String>(props, new StringSerializer(), new StringSerializer());

		producer.initTransactions();// 开启事务
		
		long start = System.currentTimeMillis();
		
		try {
			producer.beginTransaction();
			for (int i = 0; i < 1000000; i++) {
				producer.send(new ProducerRecord<>("my-topic", Integer.toString(i), Integer.toString(i)));
			}
			producer.commitTransaction();// 提交事务
		} catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
			// We can't recover from these exceptions, so our only option is to close the
			// producer and exit.
			producer.close();
		} catch (KafkaException e) {
			// For all other exceptions, just abort the transaction and try again.
			producer.abortTransaction();
		}
		producer.close();
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("共计耗时：" + time + "ms");
	}
	
	
	/**
	 * 流生产者测试
	 * 自动提交
	 */
	@Test
	public void streamProducerTest() {
		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			producer.send(new ProducerRecord<String, String>("my-stream-input-topic", Integer.toString(i), Integer.toString(i)));
		}
		producer.close();
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("共计耗时：" + time + "ms");
	}

}
