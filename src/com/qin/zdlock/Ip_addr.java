package com.qin.zdlock;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Ip_addr {
	// public static String ip_address="10.3.16.130";
//	public static String server_ip_address = "172.16.1.214";
    //public static String server_ip_address = "192.168.0.4";
    //public static String server_ip_address = "75.168.63.100";
    public static String server_ip_address = "128.101.106.172:8888";
    public static String server_ip_address_withoutPort = "128.101.106.172";
    //public static String server_ip_address = "75.161.177.31";

    //public static String server_ip_address = "75.168.63.100";
	// public static String ip_address="172.16.1.222";
	// public static String self_ip="";

	public static String getLocalIPAddress() throws SocketException {
		for (Enumeration<NetworkInterface> en = NetworkInterface
				.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
					.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress()
						&& (inetAddress instanceof Inet4Address)) {
					return inetAddress.getHostAddress().toString();
				}
			}
		}
		return "null";
	}
}
