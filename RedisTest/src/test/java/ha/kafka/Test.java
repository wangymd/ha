package ha.kafka;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {

	public static void main(String[] args) {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			String hostName = addr.getHostName().toString(); 
			System.out.println(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
