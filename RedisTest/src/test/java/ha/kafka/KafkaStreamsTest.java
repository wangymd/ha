package ha.kafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.Before;
import org.junit.Test;

import ha.BaseTest;

/**
 * Kafka流测试
 * @author wangym
 *
 */
public class KafkaStreamsTest extends BaseTest {

	//Map<String, Object> props = new HashMap<>();
	
	Properties props = new Properties();

	KafkaConsumer<String, String> consumer = null;

	@Before
	public void initKafka() {
		 props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
		 props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.229.13:9092,192.168.229.13:9093,192.168.229.13:9094");
		 props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		 props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
	}

	/**
	 * 流测试
	 */
	@SuppressWarnings("resource")
	@Test
	public void test1() {
		 StreamsBuilder builder = new StreamsBuilder();
		 builder.<String, String>stream("my-stream-input-topic")
		 .mapValues(value -> {
	            System.out.println(value);
	            return value;
	        })
		 .to("my-stream-output-topic");
		 KafkaStreams streams = new KafkaStreams(builder.build(), props);
		 streams.start();
		 
		 while(true) {
			 
		 }
	}

}
