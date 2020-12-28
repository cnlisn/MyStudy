package com.lisn.mystudy.socket.TCP;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    @Test
    public void main() throws IOException {
        //1.创建服务端Socket并明确端口号
        ServerSocket serverSocket = new ServerSocket(10000);
        //2.获取到客户端的Socket
        Socket socket = serverSocket.accept();
        //3.通过客户端的Socket获取到输入流
        InputStream inputStream = socket.getInputStream();
        //4.通过输入流获取到客户端传递的数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }

        //5.关闭流
        socket.close();
        serverSocket.close();
    }
}