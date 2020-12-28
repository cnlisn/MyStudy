package com.lisn.mystudy.socket.TCP;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

//客户端
public class TCPClient {

    @Test
    public void main() throws IOException {

        //1.创建TCP客户端Socket服务
        Socket client = new Socket();
        //2.与服务端进行连接
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10000);
        client.connect(address);
        //3.连接成功后获取客户端Socket输出流
        OutputStream outputStream = client.getOutputStream();
        //4.通过输出流往服务端写入数据
        outputStream.write("hello server".getBytes());
        //5.关闭流
        client.close();
    }
}