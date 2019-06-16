package ha.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Before;
import org.junit.Test;

import ha.BaseTest;

/**
 * Kafka消费者
 * @author wangym
 *
 */
public class KafkaStreamsConsumerTest extends BaseTest {

	Properties props = new Properties();

	KafkaConsumer<String, String> consumer = null;

	@Before
	public void initKafka() {
		props.put("bootstrap.servers", "192.168.229.13:9092,192.168.229.13:9093,192.168.229.13:9094");
		props.setProperty("group.id", "test");
		props.setProperty("enable.auto.commit", "true");
		props.setProperty("auto.commit.interval.ms", "1000");
		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	}

	/**
	 * 流测试-消费者
	 * 自动提交
	 */
	@SuppressWarnings("resource")
	@Test
	public void streamConsumerTest() {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("my-stream-output-topic"));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, String> record : records)
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
		}
	}

}
