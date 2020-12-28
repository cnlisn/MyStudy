package com.lisn.mystudy.socket.UDP;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 聊天实例 客户1
 */
public class UDPSocket1 {

    @Test
    public void main() {
        System.out.println("UDPSocket1 start");
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        receive();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //接收消息方法
    private static void receive() throws IOException {
        System.out.println("UDOSocket1 Receiver Start...");

        //1.创建udp的socket服务,并声明端口号
        DatagramSocket ds = new DatagramSocket(6666);
        //无限循环，一直处于接收状态
        while (true) {
            //2.创建接收数据的数据包
            byte[] bytes = new byte[1024];
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length);

            //3.将数据接收到数据包中
            ds.receive(dp);

            //4.解析数据
            String content = new String(dp.getData(), 0, dp.getLength());
            System.out.println("UDPSocket1 Receive:" + content);
        }
    }

    @Test
    public void send() throws IOException {
        //1.创建socket服务
        DatagramSocket ds = new DatagramSocket();

        //将键盘输入的信息转换成输入流再放入到缓冲区
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while ((line = br.readLine()) != null) {
            //2.封装数据
            byte[] bytes = line.getBytes();
            //地址
            InetAddress address = InetAddress.getByName("127.0.0.1");
            //参数：数据、长度、地址、端口
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length, address, 7777);

            //3.发送数据包
            ds.send(dp);
        }

        //4.关闭socket服务
        ds.close();
    }

    @Test
    public void send1() throws IOException {
        //1.创建socket服务
        DatagramSocket ds = new DatagramSocket();

        //2.封装数据
        byte[] bytes = "hello world".getBytes();
        //地址
        InetAddress address = InetAddress.getByName("127.0.0.1");
        //参数：数据、长度、地址、端口
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, address, 7777);

        //3.发送数据包
        ds.send(dp);

        //4.关闭socket服务
        ds.close();
    }
}

