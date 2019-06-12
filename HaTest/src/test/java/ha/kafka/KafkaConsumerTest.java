package ha.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;

import ha.BaseTest;

/**
 * Kafka消费者
 * @author wangym
 *
 */
public class KafkaConsumerTest extends BaseTest {

	Properties props = new Properties();

	KafkaConsumer<String, String> consumer = null;

	@Before
	public void initKafka() {
		// props.put("bootstrap.servers", "192.168.229.13:9092");
		props.put("bootstrap.servers", "192.168.229.13:9092,192.168.229.13:9093,192.168.229.13:9094");
		props.setProperty("group.id", "test");
		props.setProperty("enable.auto.commit", "true");
		props.setProperty("auto.commit.interval.ms", "1000");
		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	}

	/**
	 * 
	 * 发送信息 自动提交
	 */
	@SuppressWarnings("resource")
	@Test
	public void test1() {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("my-topic"));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, String> record : records)
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
		}
	}

	/**
	 * 
	 * 发送信息 手动提交
	 */
	@Test
	public void test2() {
		props.setProperty("enable.auto.commit", "false");

		consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("foo", "bar"));
		final int minBatchSize = 200;
		List<ConsumerRecord<String, String>> buffer = new ArrayList<ConsumerRecord<String, String>>();
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, String> record : records) {
				buffer.add(record);
			}
			if (buffer.size() >= minBatchSize) {
				// insertIntoDb(buffer);
				consumer.commitSync();// 手动提交
				buffer.clear();
			}
		}
	}

	/**
	 * 
	 * 发送信息 手动提交
	 */
	@Test
	public void test3() {
		try {
			consumer = new KafkaConsumer<String, String>(props);
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
				for (TopicPartition partition : records.partitions()) {
					List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
					for (ConsumerRecord<String, String> record : partitionRecords) {
						System.out.println(record.offset() + ":" + record.value());
					}
					long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
					consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
				}
			}
		} finally {
			consumer.close();
		}
	}
	
	/**
	 * 流消费者测试
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
