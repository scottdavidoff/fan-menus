
import java.awt.*;
import java.io.*;
import java.net.*;

public class ClientSocket
{
    boolean DEBUG = false;
    InetAddress address;
    TextField portField;
    Label display;
    DatagramSocket socket;
    
    String hostName = "";
    
    String userID = "";
    

    private static int PACKET_SIZE = 1024;
    
    public ClientSocket(String hostName) {
    		this.hostName = hostName;
    		init();
    }

    public void init() {
    		
    	    try {
    			address = InetAddress.getByName(hostName);
    			//address = InetAddress.getLocalHost();
    			//System.out.println(address.getHostAddress());
    		} catch (UnknownHostException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
        try {
            socket = new DatagramSocket();
        } catch (IOException e) {
            System.out.println("Couldn't create new DatagramSocket");
            return;
        }
        
        requestID();

        //long fakeTime = (new Date()).getTime();
        //send("U12\t17\ttargetName\t" + fakeTime + "\t12312\t289873\tactionName");
        //send("hi again");
        //send("write");
    }
    
    private void requestID() {
    		send("getID");
    		
    		byte[] buf = new byte[PACKET_SIZE];
        DatagramPacket packet;
                    // receive request
        packet = new DatagramPacket(buf, PACKET_SIZE);
        try {
			socket.receive(packet);
			userID = (new String(packet.getData())).trim();
			//System.out.println("I'm " + userID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		
    }
    
    public String getUserID() {
    		return userID;
    }
    
    public void send(String s) {
    	
    		DatagramPacket packet;
        byte[] sendBuf = new byte[PACKET_SIZE];
        sendBuf = s.getBytes();
 
        //System.out.println("sending packet, length=" + sendBuf.length);
        packet = new DatagramPacket(sendBuf, sendBuf.length, address, 2001);
        try {
			socket.send(packet);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //System.out.println("packet sent");
    	
    }
    
    public void writeFile() {
    		send("write");
    }

}
