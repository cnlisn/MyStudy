package com.lisn.mystudy.socket.TCP.upload;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPUploadClient {
    @Test
    public void main() throws IOException {

        //1.创建TCP客户端Socket服务
        Socket client = new Socket();

        //2.与服务端进行连接
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10001);
        client.connect(address);

        //3.读取客户端文件
        FileInputStream fis = new FileInputStream("/uploadFile.jpg");

        //4.获取输出流
        OutputStream outputStream = client.getOutputStream();

        //5.将文件写入到服务端
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = fis.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }

        //6.通知服务器数据写入完毕
        client.shutdownOutput();

        //7.读取服务端响应的数据
        InputStream inputStream = client.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = br.readLine();
        System.out.println(line);

        //8.关闭流
        inputStream.close();
        fis.close();
        client.close();
    }
}