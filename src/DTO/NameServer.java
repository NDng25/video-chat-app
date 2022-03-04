package DTO;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NameServer
{
public static InetAddress getServerAddress() throws UnknownHostException 
{
	return InetAddress.getByName("localhost");
	//123.26.63.165
	//localhost
	//123.19.188.56
}
}
