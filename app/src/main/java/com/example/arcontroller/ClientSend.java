package com.example.arcontroller;

import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientSend implements Runnable {
    private String PhoneMessage;
    private String ip = "131.179.176.74";
    private int port = 8051;

    public ClientSend(String v) {
        PhoneMessage = v;
    }

    @Override
    public void run() {
        try {
            final DatagramSocket udpSocket = new DatagramSocket((int) (Math.random() * 2000 + 5000));
            final InetAddress serverAddr = InetAddress.getByName(ip);
            byte[] buf = (PhoneMessage).getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, port);
            udpSocket.send(packet);
            Log.d("LRP_Demo_ClientSend", "Data sent out: " + this.PhoneMessage);
            udpSocket.close();
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "IO Error:", e);
        }
    }
}
