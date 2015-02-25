package org.flexymouse.listener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.flexymouse.listener.event.AbstractEvent;
import org.flexymouse.listener.event.ButtonClickEvent;

public class Connection implements Runnable {
    
    private int port;

    private DatagramSocket socket;
    
    private Application app;
    
    static public final int DEFAULT_PORT = 1069;

    public Connection(Application a, int port) throws SocketException {
        this.port = port;
        this.socket = new DatagramSocket(port);
        this.app = a;
    }

    public void run() {
        try {
            byte[] inputBuffer = new byte[Protocol.DEFAULT_PACKET_SIZE];
            System.out.println("FlexyMouseListener is waiting data on port " + port);
            while (true) {
                // Receive data
                DatagramPacket inputPacket = new DatagramPacket(inputBuffer, inputBuffer.length);
                socket.receive(inputPacket);
                String inputData = new String(inputPacket.getData());
                String data = inputData.substring(0, inputData.indexOf(Protocol.BREAKING_STRING));
                // System.out.println("DATA RECEIVED : " + data);

                // TODO manage status
                this.app.getStatusBar().updateConnectionStatus(true, inputPacket.getAddress().getHostAddress().toString());

                // Send a response ?
                // byte[] sendData = new byte[1024];
                // InetAddress IPAddress = receivePacket.getAddress();
                // int port = receivePacket.getPort();
                // String capitalizedSentence = sentence.toUpperCase();
                // sendData = capitalizedSentence.getBytes();
                // DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                // serverSocket.send(sendPacket);

                // Extract data and construct a new event
                AbstractEvent event = Protocol.extractEventFromData(data);

                // Dispatch event to the statusBar
                this.app.getStatusBar().onFlexyMouseEvent(event);

                // Dispatch event to the current view
                if (event != null) {
                    this.app.getCurrentView().onFlexyMouseEvent(event);
                }

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            socket.close();
        }
    }
}
