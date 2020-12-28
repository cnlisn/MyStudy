package com.lisn.mystudy.socket.UDP;

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 分别启动发送方和接收方，我们来看一下打印结果
 * 发送方：
 * Sender Start...
 *
 * 接收方
 * Receiver Start...
 * address:/192.168.31.137---port:65037---content:Did you recite words today
 *
 * 成功接收到消息，并打印出发送方IP和端口，下面我来个大家撸一遍步骤
 * 发送方：
 *
 * 首先创建udp的socket服务
 * 将需要发送的数据放在数据包DatagramSocket中，DatagramSocket会根据UDP协议对数据包、IP、端口号进行封装
 * 通过udp的socket服务将数据包发送
 * 最后将udp服务关闭
 * 接收方：
 *
 * 创建udp的socket服务，并且明确自己的端口号
 * 创建DatagramSocket用来解析数据接收到的数据包
 * 将数据接收到数据包DatagramSocket中
 * 通过DatagramSocket解析数据
 * 关闭服务
 *
 * 作者：zskingking
 * 链接：https://www.jianshu.com/p/b04930d2b85e
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */

public class UDPSocket {

    /**
     * 发送方UDP
     */
    @Test
    public void test_send() throws IOException {

        System.out.println("Sender Start...");

        //1.创建socket服务
        DatagramSocket ds = new DatagramSocket();

        //2.封装数据
        String str = "Did you recite words today";
        byte[] bytes = str.getBytes();
        //地址
        InetAddress address = InetAddress.getByName("127.0.0.1");
        //参数：数据、长度、地址、端口
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, address, 6666);

        //3.发送数据包
        ds.send(dp);

        //4.关闭socket服务
        ds.close();
    }

    /**
     * 接收方UDP
     */
    @Test
    public void test_receive() throws IOException {

        System.out.println("Receiver Start...");

        //1.创建udp的socket服务,并声明端口号
        DatagramSocket ds = new DatagramSocket(6666);

        //2.创建接收数据的数据包
        byte[] bytes = new byte[1024];
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length);

        //3.将数据接收到数据包中，为阻塞式方法
        ds.receive(dp);

        //4.解析数据
        InetAddress address = dp.getAddress();//发送方IP
        int port = dp.getPort();//发送方端口
        String content = new String(dp.getData(), 0, dp.getLength());
        System.out.println("address:" + address + "---port:" + port + "---content:" + content);

        //关闭服务
        ds.close();
    }
}