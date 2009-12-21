package pl.edu.agh.iptv.servlet;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MulticastSet {

	public static Set<String> multicastIpSet = new HashSet<String>();
	static Random random = new Random();

	public static String getMulticastIp() {
		String multicastIp = null;
		boolean isProperAddress = false;
		do {
			Integer first = random.nextInt(15) + 224;
			Integer second = random.nextInt(255);
			Integer third = random.nextInt(255);
			Integer fourth = random.nextInt(255);
			multicastIp = first + "." + second + "." + third + "." + fourth;
			
			if(!multicastIpSet.contains(multicastIp))
			{
				multicastIpSet.add(multicastIp);
				isProperAddress = true;
			}
		} while (!isProperAddress);
		
		return multicastIp;
	}

}
